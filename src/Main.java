// Bron FCFS: https://www.thejavaprogrammer.com/java-program-first-come-first-serve-fcfs-scheduling-algorithm/?fbclid=IwAR1GqmM53vW0g3DXpoccgINH7YXbX2_22euhNiuYnysxKm6TZLWsGJVm0fk
// Bron SRT: https://javahungry.blogspot.com/2013/11/shortest-remaining-time-first-srt-preemptive-non-preemptive-sjf-scheduling-algorithm-with-example-java-program-code.html#:~:text=Shortest%20remaining%20time%20(%20SRT%20)%20scheduling,end%20of%20its%20CPU%20burst%20.


import java.util.Scanner;

public class Main {
    static int aantalProcessen;
    static int[] pid;
    static int[] arrivaltime;
    static int[] servicetime;
    static int[] remainingtime;
    static int[] endtime;
    static int[] turnaroundtime;
    static int[] waittime;
    static int[] time;                                              // voor visueel nakijken
    static int nodigeTijd;
    static int temp;
    static float avgnormalizedturnaroundtime = 0, avgturnaroundtime = 0, avgwaittime = 0;

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Geef het aantal processen in (5/10000/20000/50000):");
        aantalProcessen = sc.nextInt();

        initialiseerArrays();
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, arrivaltime, servicetime);

        System.out.print("Geef het scheduling algortime in (FCFS/SRT/HRRN):");
        String algoritme = sc.next();
        switch (algoritme) {
            case "FCFR" -> berekenFCFS();
            case "SRT" -> berekenSRT();
            case "HRRN" -> berekenHRRN();
            default -> {
            }
        }
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


    // FCFS zonder queue
    static void berekenFCFS() {
        for(int i = 0 ; i < aantalProcessen; i++) {
            if(i == 0)
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
            avgnormalizedturnaroundtime += turnaroundtime[i]/servicetime[i];
        }
    }

    // SRT
    static void berekenSRT() {
        nodigeTijd = nodigeTijdBerekenen();
        System.arraycopy(servicetime, 0, remainingtime, 0, aantalProcessen);
        time = new int[nodigeTijd];                   // voor visueel nakijken

        for(int i = 0; i < nodigeTijd; i++) {
            int processWithSRT = kleinsteTijdSRT(i);
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
    private static int kleinsteTijdSRT(int huidigeTijd) {
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
        nodigeTijd = 0;
        for (int i = 0; i<aantalProcessen; i++)
            nodigeTijd += servicetime[i];
        return nodigeTijd;
    }


    // HRRN
    static void berekenHRRN() {
    }
}
