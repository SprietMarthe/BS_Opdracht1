import java.util.Scanner;

public class SRT {
    static int aantalProcessen;
    static int[] pid;
    static int[] arrivaltime;
    static int[] servicetime;
    static int[] remainingtime;
    static int[] endtime;
    static int[] turnaroundtime;
    static int[] waittime;
    static int[] time;                                              // voor visueel nakijken
    static int nodigeTijd = 0;
    static float avgnormalizedturnaroundtime = 0, avgturnaroundtime = 0, avgwaittime = 0;

    public static void main(String[] argv){
        Scanner sc = new Scanner(System.in);
        System.out.print("Geef het aantal processen in (5/10000/20000/50000):");
        aantalProcessen = sc.nextInt();

        initialiseerArrays();
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, arrivaltime, servicetime);

        // Shortest Remaining Time
        berekenen();

        printResultaten();
        sc.close();
    }

    private static void initialiseerArrays() {
        pid = new int[aantalProcessen];
        arrivaltime = new int[aantalProcessen];
        servicetime = new int[aantalProcessen];
        remainingtime = new int[aantalProcessen];
        endtime = new int[aantalProcessen];
        turnaroundtime = new int[aantalProcessen];
        waittime = new int[aantalProcessen];
    }

    private static void printResultaten() {
        System.out.println("\npid  arrival  service  end turnaround wait - time ");
        for(int  i = 0; i< aantalProcessen;  i++) {
            System.out.println(pid[i] + "\t\t" + arrivaltime[i] + "\t\t" + servicetime[i] + "\t\t" + endtime[i] + "\t\t" + turnaroundtime[i] + "\t\t"  + waittime[i] ) ;
        }
        System.out.print("Volgorde in waarin de processen worden uitgevoerd: ");
        for(int i = 0; i<nodigeTijd; i++) System.out.print(time[i] + " ");
        System.out.print("\n\naverage turnaround time: "+ (avgturnaroundtime/aantalProcessen));
        System.out.print("\naverage normalized turnaround time: "+ (avgnormalizedturnaroundtime/aantalProcessen));
        System.out.print("\naverage wait time: "+ (avgwaittime/aantalProcessen));

    }

    private static void berekenen() {
        nodigeTijd = nodigeTijdBerekenen();
        System.arraycopy(servicetime, 0, remainingtime, 0, aantalProcessen);
        time = new int[nodigeTijd];                   // voor visueel nakijken

        for(int i = 0; i < nodigeTijd; i++) {
            int processWithSRT = kleinsteTijd(i);
            remainingtime[processWithSRT]--;
            if (remainingtime[processWithSRT] == 0)
                endtime[processWithSRT] = i + 1;
            time[i] = processWithSRT + 1;                          // voor visueel nakijken
        }

        for (int i = 0; i<aantalProcessen; i++){
            waittime[i] = endtime[i]-arrivaltime[i]-servicetime[i];
            turnaroundtime[i] = servicetime[i] + waittime[i];

            avgwaittime += waittime[i];
            avgturnaroundtime += turnaroundtime[i];
            avgnormalizedturnaroundtime += turnaroundtime[i]/servicetime[i];
        }
    }

    private static int kleinsteTijd(int huidigeTijd) {
        int processWithSRT = 0;
        int min = nodigeTijd;
        for (int i = 0; i<aantalProcessen; i++)
            if (arrivaltime[i]<=huidigeTijd && remainingtime[i]<min && remainingtime[i] != 0) {
                min = remainingtime[i];
                processWithSRT = i;
            }
        return processWithSRT;
    }

    private static int nodigeTijdBerekenen() {
        int nodigeTijd = 0;
        for (int i = 0; i<aantalProcessen; i++)
            nodigeTijd += servicetime[i];
        return nodigeTijd;
    }
}


