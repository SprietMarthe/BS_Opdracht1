// Bron FCFS: https://www.thejavaprogrammer.com/java-program-first-come-first-serve-fcfs-scheduling-algorithm/?fbclid=IwAR1GqmM53vW0g3DXpoccgINH7YXbX2_22euhNiuYnysxKm6TZLWsGJVm0fk
// Bron SRT: https://javahungry.blogspot.com/2013/11/shortest-remaining-time-first-srt-preemptive-non-preemptive-sjf-scheduling-algorithm-with-example-java-program-code.html#:~:text=Shortest%20remaining%20time%20(%20SRT%20)%20scheduling,end%20of%20its%20CPU%20burst%20.


import java.util.Scanner;

public class Main {
    static int aantalProcessen, nodigeTijd;
    static int[] pid, arrivaltime, servicetime, remainingtime, endtime, turnaroundtime, waittime, time;                                              // voor visueel nakijken
    static float avgnormalizedturnaroundtime, avgturnaroundtime, avgwaittime;

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Geef het scheduling algortime in (FCFS/SRT/HRRN):");
        String algoritme = sc.next();
        System.out.print("Geef het aantal processen in (5/10000/20000/50000):");
        aantalProcessen = sc.nextInt();

        initialiseerArrays();
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, arrivaltime, servicetime);
        nodigeTijd = nodigeTijdBerekenen();

        switch (algoritme) {
            case "FCFS" -> berekenFCFS();
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

        avgnormalizedturnaroundtime = 0;
        avgturnaroundtime = 0;
        avgwaittime = 0;
    }
    private static void berekenGevraagde(int i) {
        waittime[i] = endtime[i]-arrivaltime[i]-servicetime[i];
        turnaroundtime[i] = servicetime[i] + waittime[i];

        avgwaittime += waittime[i];
        avgturnaroundtime += turnaroundtime[i];
        avgnormalizedturnaroundtime += turnaroundtime[i]/servicetime[i];
    }
    private static int nodigeTijdBerekenen() {
        nodigeTijd = 0;
        for (int i = 0; i<aantalProcessen; i++)
            nodigeTijd += servicetime[i];
        time = new int[nodigeTijd];
        return nodigeTijd;
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




    // --------------------First Come First Serve (zonder queue) (FCFS)--------------------
    static void berekenFCFS() {
        for(int i = 0 ; i < aantalProcessen; i++) {
            if( arrivaltime[i] > endtime[i-1] || i == 0)
                endtime[i] = arrivaltime[i] + servicetime[i];
            else
                endtime[i] = endtime[i-1] + servicetime[i];
            berekenGevraagde(i);
        }
    }





    // --------------------Shortest Remaining Time (SRT)--------------------
    static void berekenSRT() {
        System.arraycopy(servicetime, 0, remainingtime, 0, aantalProcessen);

        for(int i = 0; i < nodigeTijd; i++) {
            int processWithSRT = kleinsteTijdSRT(i);
            remainingtime[processWithSRT]--;
            if (remainingtime[processWithSRT] == 0)
                endtime[processWithSRT] = i + 1;
            time[i] = processWithSRT + 1;                          // voor visueel
        }
        for (int i = 0; i<aantalProcessen; i++) {
            berekenGevraagde(i);
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





    // --------------------Highest Response Ratio Next (HRRN)--------------------
    static void berekenHRRN() {
        for(int i = 0; i < nodigeTijd; i++) {
            int processWithHRRN = grootsteTijdHRRN(i);
            endtime[processWithHRRN] = i + servicetime[processWithHRRN];
            time[i] = processWithHRRN + 1;                          // voor visueel
            i += servicetime[processWithHRRN] - 1;
        }
        for (int i = 0; i<aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }
    private static int grootsteTijdHRRN(int huidigeTijd) {
        int processWithHRRN = 0;
        double max = -1;
        double genormaliseerdeTAT = 0;
        for (int i = 0; i<aantalProcessen; i++) {
            genormaliseerdeTAT = (huidigeTijd - arrivaltime[i]) / servicetime[i];
            if (arrivaltime[i] <= huidigeTijd && genormaliseerdeTAT>max && endtime[i] == 0) {
                max = genormaliseerdeTAT;
                processWithHRRN = i;
            }
        }
        return processWithHRRN;
    }
}
