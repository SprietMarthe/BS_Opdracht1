// Bron FCFS: https://www.thejavaprogrammer.com/java-program-first-come-first-serve-fcfs-scheduling-algorithm/?fbclid=IwAR1GqmM53vW0g3DXpoccgINH7YXbX2_22euhNiuYnysxKm6TZLWsGJVm0fk
// Bron SRT: https://javahungry.blogspot.com/2013/11/shortest-remaining-time-first-srt-preemptive-non-preemptive-sjf-scheduling-algorithm-with-example-java-program-code.html#:~:text=Shortest%20remaining%20time%20(%20SRT%20)%20scheduling,end%20of%20its%20CPU%20burst%20.


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    static int aantalProcessen, huidigeTijd;
    static int[] pid, aankomsttijd, bedieningstijd, resterendeTijd, starttijd, eindtijd, omlooptijd, wachttijd;
    static double[] genormaliseerdeOmlooptijd;                                              // voor visueel nakijken
    static double gemiddeldeOmlooptijd, gemiddeldeGenormaliseerdeOmlooptijd, gemiddeldeWachttijd;

    static Queue<Integer> readyQueue, nogToekomendeProcesses, queue1 , queue2, queue3, queue4, queue5;

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Geef het scheduling algortime in (FCFS/SJF/SRT/HRRN/RR/MLFBv1/MLFBv2):");
        String algoritme = sc.next();
        System.out.print("Geef het aantal processen in (5/10000/20000/50000):");
        aantalProcessen = sc.nextInt();

        initialiseerArrays();
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, aankomsttijd, bedieningstijd);

        switch (algoritme) {
            case "FCFS" -> berekenFCFS();
            case "SJF" -> berekenSJF();
            case "SRT" -> berekenSRT();
            case "HRRN" -> berekenHRRN();
            case "RR" -> {
                System.out.print("Geef een timeslice in:");
                int timeslice = sc.nextInt();
                berekenRR(timeslice);}
            case "MLFBv1" -> berekenMLFB(1);
            case "MLFBv2" -> berekenMLFB(2);
            default -> {
            }
        }
        printResultaten();
        sc.close();
    }

    private static void initialiseerArrays() {
        pid = new int[aantalProcessen];
        aankomsttijd = new int[aantalProcessen];
        bedieningstijd = new int[aantalProcessen];
        resterendeTijd = new int[aantalProcessen];
        starttijd = new int[aantalProcessen];
        eindtijd = new int[aantalProcessen];
        omlooptijd = new int[aantalProcessen];
        genormaliseerdeOmlooptijd = new double[aantalProcessen];
        wachttijd = new int[aantalProcessen];

        gemiddeldeOmlooptijd = 0;
        gemiddeldeGenormaliseerdeOmlooptijd = 0;
        gemiddeldeWachttijd = 0;
    }
    private static void berekenGevraagde(int i) {
        wachttijd[i] = eindtijd[i]- aankomsttijd[i]-bedieningstijd[i];
        omlooptijd[i] = bedieningstijd[i] + wachttijd[i];
        genormaliseerdeOmlooptijd[i] = (double)omlooptijd[i]/(double)bedieningstijd[i];

        gemiddeldeWachttijd += wachttijd[i];
        gemiddeldeGenormaliseerdeOmlooptijd += genormaliseerdeOmlooptijd[i];
        gemiddeldeOmlooptijd += omlooptijd[i];
    }
    private static boolean overigeResterendeTijd() {
        int resterendeTijd=0;
        for (int i=0; i<aantalProcessen; i++){
            resterendeTijd += Main.resterendeTijd[i];
        }
        return resterendeTijd > 0;
    }
    private static boolean overigeOnafgewerkteProcessen() {
        int aantalUnfinished=0;
        for (int i=0; i<aantalProcessen-1; i++){
            if (eindtijd[i] == 0) aantalUnfinished++;
        }
        return aantalUnfinished > 0;
    }
    private static void printResultaten() {
        System.out.println("\npid\t\taankomst\t\tbedienings\t\tstart\t\teind\t\tomloop\t\tgenormaliseerde omloop\t\twacht - tijd ");
        for(int  i = 0; i< aantalProcessen;  i++) {
            System.out.println(pid[i] + "\t\t" + aankomsttijd[i] + "\t\t\t\t" + bedieningstijd[i] + "\t\t\t\t" + starttijd[i]+ "\t\t\t" + eindtijd[i] + "\t\t\t" + omlooptijd[i] + "\t\t\t"  + genormaliseerdeOmlooptijd[i] + "\t\t\t\t\t\t\t" + wachttijd[i] ) ;
        }
        System.out.print("\ngemiddelde omlooptijd: "+ (gemiddeldeOmlooptijd /aantalProcessen));
        System.out.print("\ngemiddelde genormaliseerde omlooptijd: "+ (gemiddeldeGenormaliseerdeOmlooptijd /aantalProcessen));
        System.out.print("\ngemiddelde wachttijd: "+ (gemiddeldeWachttijd /aantalProcessen));
    }




    // -------------------- First Come First Serve (FCFS) --------------------
    static void berekenFCFS() {
        for(int i = 0 ; i < aantalProcessen; i++) {
            if( i == 0 || aankomsttijd[i] > eindtijd[i-1] ) {
                starttijd[i] = aankomsttijd[i];
                eindtijd[i] = aankomsttijd[i] + bedieningstijd[i];
            }
            else {
                starttijd[i] = eindtijd[i-1];
                eindtijd[i] = starttijd[i] + bedieningstijd[i];
            }
            berekenGevraagde(i);
        }
    }



    // ------------------- Shortest Job First (SJF) --------------------------
    static void berekenSJF(){
        int[] bedieningstijdKopie = new int[aantalProcessen];
        System.arraycopy(bedieningstijd, 0, bedieningstijdKopie, 0, aantalProcessen);

        int aantalJiffys = 0;
        for (int i=0; i<aantalProcessen; i++) {
            int min = Integer.MAX_VALUE;
            int sPID = 0;
            for(int j=0; j<aantalProcessen; j++ ) {
                if (aankomsttijd[j] <= aantalJiffys) {
                    if(bedieningstijdKopie[j]<min) {
                        min = bedieningstijdKopie[j];
                        sPID = j;
                    }
                }
            }
            starttijd[sPID] = aantalJiffys;
            eindtijd[sPID] = aantalJiffys + bedieningstijd[sPID];
            berekenGevraagde(sPID);

            //zodat deze niet meer als minimum kan gezien worden
            bedieningstijdKopie[sPID] = Integer.MAX_VALUE;
            aantalJiffys += bedieningstijd[sPID];
        }
    }


    // -------------------- Shortest Remaining Time (SRT) --------------------
    static void berekenSRT() {
        System.arraycopy(bedieningstijd, 0, resterendeTijd, 0, aantalProcessen);
        int procesMetSRT;
        huidigeTijd=0;
        while (overigeResterendeTijd()){
            procesMetSRT = kleinsteTijdSRT();
            if (procesMetSRT>-1) {
                if (starttijd[procesMetSRT] == 0 && aankomsttijd[procesMetSRT] != 0) starttijd[procesMetSRT] = huidigeTijd;
                resterendeTijd[procesMetSRT]--;
                if (resterendeTijd[procesMetSRT] == 0)
                    eindtijd[procesMetSRT] = huidigeTijd + 1;
            }
            huidigeTijd++;
        }
        for (int i = 0; i<aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }
    private static int kleinsteTijdSRT() {
        int procesMetSRT = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i<aantalProcessen; i++)
            if (aankomsttijd[i]<=huidigeTijd && resterendeTijd[i]<min && resterendeTijd[i] != 0) {
                min = resterendeTijd[i];
                procesMetSRT = i;
            }
        return procesMetSRT;
    }





    // -------------------- Highest Response Ratio Next (HRRN) --------------------
    static void berekenHRRN() {
        int procesMetHRRN;
        huidigeTijd=0;
        while (overigeOnafgewerkteProcessen()){
            procesMetHRRN = grootsteTijdHRRN();
            if(procesMetHRRN>-1) {
                eindtijd[procesMetHRRN] = huidigeTijd + bedieningstijd[procesMetHRRN];
                starttijd[procesMetHRRN] = eindtijd[procesMetHRRN] -bedieningstijd[procesMetHRRN];
                huidigeTijd += bedieningstijd[procesMetHRRN];
            }
            else
                huidigeTijd++;
        }
        for (int i = 0; i<aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }
    private static int grootsteTijdHRRN() {
        int procesMetHRRN = -1;
        double max = -1;
        double genormaliseerdeTAT = 0;
        for (int i = 0; i<aantalProcessen; i++) {
            genormaliseerdeTAT = (huidigeTijd - aankomsttijd[i]) / bedieningstijd[i];
            if (aankomsttijd[i] <= huidigeTijd && genormaliseerdeTAT>max && eindtijd[i] == 0) {
                max = genormaliseerdeTAT;
                procesMetHRRN = i;
            }
        }
        return procesMetHRRN;
    }






    // -------------------- Round Robin (RR) --------------------
    static void berekenRR(int timeslice) {
        System.arraycopy(bedieningstijd, 0, resterendeTijd, 0, aantalProcessen);
        huidigeTijd = 0;
        vulNogToekomendeProcesses();
        int proces = -1;
        while (overigeResterendeTijd()) {
            checkOpToekomendeProcessesRR();
            if ( proces != -1 && resterendeTijd[proces] > 0)
                readyQueue.add(proces);
            proces = schedulingProcesVanReadyQueueRR(timeslice);
        }
        for (int i = 0; i < aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }
    private static void vulNogToekomendeProcesses() {
        readyQueue = new LinkedList<>();
        nogToekomendeProcesses = new LinkedList<>();
        for (int i = 0; i<aantalProcessen; i++)
            nogToekomendeProcesses.add(i);
    }
    private static void checkOpToekomendeProcessesRR() {
        int toekomendproces;
        while (!nogToekomendeProcesses.isEmpty() && aankomsttijd[nogToekomendeProcesses.peek()] == huidigeTijd) {
            toekomendproces = nogToekomendeProcesses.remove();
            readyQueue.add(toekomendproces);
            starttijd[toekomendproces] = huidigeTijd;
        }
    }
    private static int schedulingProcesVanReadyQueueRR(int timeslice) {
        if (readyQueue.isEmpty())
            huidigeTijd++;
        else {
            int processWithRR = readyQueue.remove();
            if (resterendeTijd[processWithRR] > timeslice) {
                for (int i = 0; i<timeslice; i++) {
                    checkOpToekomendeProcessesRR();
                    huidigeTijd++;
                }
                resterendeTijd[processWithRR] -= timeslice;
                return processWithRR;
            } else if (resterendeTijd[processWithRR] > 0 && resterendeTijd[processWithRR] <= timeslice ) {
                for (int i = 0; i< resterendeTijd[processWithRR]; i++) {
                    checkOpToekomendeProcessesRR();
                    huidigeTijd++;
                }
                eindtijd[processWithRR] = huidigeTijd;
                resterendeTijd[processWithRR] = 0;
            }
        }
        return -1;
    }





    // -------------------- MultiLevel FeedBack (MLFB) --------------------
    static void berekenMLFB(int versie){
        maakQueues();
        System.arraycopy(bedieningstijd, 0, resterendeTijd, 0, aantalProcessen);
        huidigeTijd = 0;
        vulNogToekomendeProcesses();
        if (versie == 1) {
            while (overigeResterendeTijd()) {
                checkOpToekomendeProcessesMLFB();
                schedulingProcessesMLFBv1();
            }
        }
        else if (versie == 2) {
            while (overigeResterendeTijd()) {
                checkOpToekomendeProcessesMLFB();
                schedulingProcessesMLFBv2();
            }
        }
        for (int i = 0; i < aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }

    private static void schedulingProcessesMLFBv2() {
        int processWithMLFB = -1;
        if (!queue1.isEmpty()) {
            processWithMLFB = queue1.remove();
            if (resterendeTijd[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue2.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue2.isEmpty()) {
            processWithMLFB = queue2.remove();
            if (resterendeTijd[processWithMLFB] > 2) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue3.add(processWithMLFB);
            } else if (resterendeTijd[processWithMLFB] > 0) {
                for (int i = 0; i< resterendeTijd[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue3.isEmpty()) {
            processWithMLFB = queue3.remove();
            if (resterendeTijd[processWithMLFB] > 4) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue4.add(processWithMLFB);
            } else if (resterendeTijd[processWithMLFB] > 0 ) {
                for (int i = 0; i< resterendeTijd[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue4.isEmpty()) {
            processWithMLFB = queue4.remove();
            if (resterendeTijd[processWithMLFB] > 8) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else if (resterendeTijd[processWithMLFB] > 0) {
                for (int i = 0; i< resterendeTijd[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue5.isEmpty()) {
            processWithMLFB = queue5.remove();
            if (resterendeTijd[processWithMLFB] > 16) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else if (resterendeTijd[processWithMLFB] > 0) {
                for (int i = 0; i< resterendeTijd[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
    }

    private static void schedulingProcessesMLFBv1() {
        int processWithMLFB = -1;
        if (!queue1.isEmpty()) {
            processWithMLFB = queue1.remove();
            if (resterendeTijd[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue2.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue2.isEmpty()) {
            processWithMLFB = queue2.remove();
            if (resterendeTijd[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue3.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue3.isEmpty()) {
            processWithMLFB = queue3.remove();
            if (resterendeTijd[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue4.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue4.isEmpty()) {
            processWithMLFB = queue4.remove();
            if (resterendeTijd[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
        else if (!queue5.isEmpty()) {
            processWithMLFB = queue5.remove();
            if (resterendeTijd[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                eindtijd[processWithMLFB] = huidigeTijd;
                resterendeTijd[processWithMLFB] = 0;
            }
        }
    }

    private static void remainingtimehigherthentimeslice(int process) {
        huidigeTijd++;
        checkOpToekomendeProcessesMLFB();
        resterendeTijd[process]--;
    }

    private static void checkOpToekomendeProcessesMLFB() {
        int toekomendprocess;
        while (!nogToekomendeProcesses.isEmpty() && aankomsttijd[nogToekomendeProcesses.peek()] == huidigeTijd) {
            toekomendprocess = nogToekomendeProcesses.remove();
            queue1.add(toekomendprocess);
            starttijd[toekomendprocess] = huidigeTijd;
        }
    }
    private static void maakQueues() {
        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
        queue3 = new LinkedList<>();
        queue4 = new LinkedList<>();
        queue5 = new LinkedList<>();
    }


}
