import java.util.Scanner;

public class FCFS_without_queue {
    static int aantalProcessen;
    static int pid[];
    static int arrivaltime[];
    static int servicetime[];
    static int endtime[];
    static int turnaroundtime[];
    static int waittime[];
    static int temp;
    static float avgwaittime = 0, avgturnaroundtime = 0;

    public static void main(String argv[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Geef het aantal processen in (5/10000/20000/50000)");
        aantalProcessen = sc.nextInt();

        initialiseerArrays();
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, arrivaltime, servicetime);

        // First Come First Serve
        sorteerOpArrivalTime();
        endtimeBerekenen();

        printResultaten();
        sc.close();
    }

    private static void initialiseerArrays() {
        pid = new int[aantalProcessen];
        arrivaltime = new int[aantalProcessen];
        servicetime = new int[aantalProcessen];
        endtime = new int[aantalProcessen];
        turnaroundtime = new int[aantalProcessen];
        waittime = new int[aantalProcessen];
    }

    private static void printResultaten() {
        System.out.println("\npid  arrival  brust  complete turn waiting");
        for(int  i = 0 ; i< aantalProcessen;  i++) {
            System.out.println(pid[i] + "  \t " + arrivaltime[i] + "\t" + servicetime[i] + "\t" + endtime[i] + "\t" + turnaroundtime[i] + "\t"  + waittime[i] ) ;
        }
        System.out.println("\naverage waiting time: "+ (avgwaittime/aantalProcessen));
        System.out.println("average turnaround time:"+(avgturnaroundtime/aantalProcessen));
}

    private static void endtimeBerekenen() {
        for(int  i = 0 ; i < aantalProcessen; i++) {
            if( i == 0)
                endtime[i] = arrivaltime[i] + servicetime[i];
            else {
                if( arrivaltime[i] > endtime[i-1])
                    endtime[i] = arrivaltime[i] + servicetime[i];
                else
                    endtime[i] = endtime[i-1] + servicetime[i];
            }
            turnaroundtime[i] = endtime[i] - arrivaltime[i];
            waittime[i] = turnaroundtime[i] - servicetime[i];
            avgwaittime += waittime[i];
            avgturnaroundtime += turnaroundtime[i];
        }
    }

    private static void sorteerOpArrivalTime() {
        for(int i = 0; i <aantalProcessen; i++) {
            for(int j = 0; j < aantalProcessen-(i+1); j++) {
                if( arrivaltime[j] > arrivaltime[j+1] ) {
                    temp = arrivaltime[j];
                    arrivaltime[j] = arrivaltime[j+1];
                    arrivaltime[j+1] = temp;
                    temp = servicetime[j];
                    servicetime[j] = servicetime[j+1];
                    servicetime[j+1] = temp;
                    temp = pid[j];
                    pid[j] = pid[j+1];
                    pid[j+1] = temp;
                }
            }
        }
    }
}
