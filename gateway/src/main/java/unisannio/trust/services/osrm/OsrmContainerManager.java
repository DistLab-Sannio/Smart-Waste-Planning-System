package unisannio.trust.services.osrm;

import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class OsrmContainerManager {
    private static final Logger log = Logger.getLogger(OsrmContainerManager.class);
    private boolean isWindows;
    
    public OsrmContainerManager() {
        this.isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    private int osrmExtract() throws IOException, InterruptedException, ExecutionException {
        Process p;

        if (containerExists("gateway-quarkus-osrm-extract-1")) {
            removeContainer("gateway-quarkus-osrm-extract-1");
        }

        String osrmExtract = "docker run --name gateway-quarkus-osrm-extract-1 -d -t -v /home/ubuntu/gateway-quarkus/gateway/data:/data ghcr.io/project-osrm/osrm-backend osrm-extract -p /opt/car.lua /data/beneventofinal.osm.pbf";
        if(this.isWindows){
            p = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", osrmExtract));
        }
        else{
            p = Runtime.getRuntime().exec(String.format("%s", osrmExtract));
        }

        return checkTerminated(p);
    }

    private int osrmPartition() throws IOException, InterruptedException, ExecutionException {
        Process p;

        if (containerExists("gateway-quarkus-osrm-partition-1")) {
            removeContainer("gateway-quarkus-osrm-partition-1");
        }

        String osrmPartition = "docker run --name gateway-quarkus-osrm-partition-1 -d -t -v /home/ubuntu/gateway-quarkus/gateway/data/:/data ghcr.io/project-osrm/osrm-backend osrm-partition /data/beneventofinal.osrm";
        if(this.isWindows){
            p = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", osrmPartition));
        }
        else{
            p = Runtime.getRuntime().exec(String.format("%s", osrmPartition));
        }

        return checkTerminated(p);
    }

    private int osrmCustomize() throws IOException, InterruptedException, ExecutionException {
        Process p;

        if (containerExists("gateway-quarkus-osrm-customize-1")) {
            removeContainer("gateway-quarkus-osrm-customize-1");
        }

        String osrmCustomize = "docker run --name gateway-quarkus-osrm-customize-1 -d -t -v /home/ubuntu/gateway-quarkus/gateway/data/:/data ghcr.io/project-osrm/osrm-backend osrm-customize /data/beneventofinal.osrm --segment-speed-file /data/updates.csv";
        if(this.isWindows){
            p = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", osrmCustomize));
        }
        else{
            p = Runtime.getRuntime().exec(String.format("%s", osrmCustomize));
        }

        return checkTerminated(p);
    }

    private int osrmContract() throws IOException, InterruptedException, ExecutionException {
        Process p;

        if (containerExists("gateway-quarkus-osrm-contract-1")) {
            removeContainer("gateway-quarkus-osrm-contract-1");
        }

        String osrmContract = "docker run --name gateway-quarkus-osrm-contract-1 -d -t -v /home/ubuntu/gateway-quarkus/gateway/data/:/data ghcr.io/project-osrm/osrm-backend osrm-contract /data/beneventofinal.osrm --segment-speed-file /data/updates.csv";
        if(this.isWindows){
            p = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", osrmContract));
        }
        else{
            p = Runtime.getRuntime().exec(String.format("%s", osrmContract));
        }

        return checkTerminated(p);
    }

    private int moveFile(String updateFile) throws IOException {
        Process p;

        String moveFileCommandWin = "move " + updateFile + "gateway-quarkus\\data\\updates.csv";
        String moveFileCommandLix = "mv " + updateFile + " /home/ubuntu/gateway-quarkus/gateway/data/updates.csv";
        if(this.isWindows){
            p = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", moveFileCommandWin));
        }
        else{
            System.out.println(moveFileCommandLix);
            p = Runtime.getRuntime().exec(String.format("%s", moveFileCommandLix));
        }

        return checkTerminated(p);
    }

    private int restartContainer(String containerName) throws IOException, InterruptedException, ExecutionException {
        if (containerExists(containerName)) {
            Process p = null;
            String restartCommand = "docker restart " + containerName;
            if(this.isWindows){
                p = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", restartCommand));
            }
            else{
                p = Runtime.getRuntime().exec(String.format("%s", restartCommand));
            }
            return checkTerminated(p);
        }        
        return -1;   
    }

    private boolean containerExists(String containerName) {
        System.out.println("docker ps -a --filter \"name=" + containerName + "\" --format \"{{.Names}}\"");
        String containerOutput = "";
        String line;
        try {
            String[] command = {"docker", "ps", "-a", "--filter", "name=" + containerName, "--format", "{{.Names}}"};
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = reader.readLine()) != null) {
                containerOutput = line;
                System.out.println("Container Output: " + containerOutput);
            }
            return containerOutput != null && containerOutput.equals(containerName);
        } catch (IOException | InterruptedException e) {
            log.error("Errore durante la verifica dell'esistenza del container", e);
            return false;
        }
    }

    private void removeContainer(String containerName) throws InterruptedException, ExecutionException {
        Process p;
        try {
            String removeCommand = "docker rm -f " + containerName;
            if(this.isWindows) {
                p = Runtime.getRuntime().exec("cmd.exe /c " + removeCommand);
            }
            else {
                p = Runtime.getRuntime().exec("" + removeCommand);
                System.out.println("REMOVED WITH COMMAND: " + removeCommand);
            }
            this.checkTerminated(p);
        } catch (IOException e) {
            log.error("Errore durante la rimozione del container", e);
        }
    }

    public int update(File file) throws IOException, ExecutionException, InterruptedException {
        try{
            System.out.println(file.getName());
            String filePath = file.getAbsolutePath();
            this.moveFile(filePath);
            log.info("File updates.csv moved!");
            this.osrmExtract();
            log.info("OSRM Extract Done!");
            this.osrmPartition();
            log.info("OSRM Partition Done!");
            this.osrmCustomize();
            log.info("OSRM Customize Done!");
            this.osrmContract();
            log.info("OSRM Contract Done!");
            this.restartContainer("gateway-quarkus-osrm-backend-1");
            log.info("OSRM Restart Done!");
        }
        catch (Exception e){
            return 1;
        }
        return 0;
    }

    private int checkTerminated(Process p) {
        int exitCode = -1;
        boolean toexit = false;

        while(!toexit){
            try {
                exitCode = p.exitValue();
                toexit = true;
            }
            catch (IllegalThreadStateException e) {
            }
        }

        return exitCode;
    }

}
