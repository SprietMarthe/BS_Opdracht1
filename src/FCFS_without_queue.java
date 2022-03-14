public class FCFS_without_queue {
    public static void main(String argv[]){
        int aantalProcessen = 10000;
        int pid[] = new int[aantalProcessen];
        int arrivaltime[] = new int[aantalProcessen];
        int servicetime[] = new int[aantalProcessen];
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, arrivaltime, servicetime);

    }
}


