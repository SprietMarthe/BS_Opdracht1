// Bron FCFS: https://www.thejavaprogrammer.com/java-program-first-come-first-serve-fcfs-scheduling-algorithm/?fbclid=IwAR1GqmM53vW0g3DXpoccgINH7YXbX2_22euhNiuYnysxKm6TZLWsGJVm0fk
// Bron SRT: https://javahungry.blogspot.com/2013/11/shortest-remaining-time-first-srt-preemptive-non-preemptive-sjf-scheduling-algorithm-with-example-java-program-code.html#:~:text=Shortest%20remaining%20time%20(%20SRT%20)%20scheduling,end%20of%20its%20CPU%20burst%20.


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    static int aantalProcessen, huidigeTijd;
    static int[] pid, arrivaltime, servicetime, remainingtime, starttime, endtime, turnaroundtime, waittime;                                              // voor visueel nakijken
    static double avgnormalizedturnaroundtime, avgturnaroundtime, avgwaittime;

    static Queue<Integer> readyqueue, nogToekomendeProcesses, queue1 , queue2, queue3, queue4, queue5;

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Geef het scheduling algortime in (FCFS/SRT/HRRN/RR/MLFBv1/MLFBv2):");
        String algoritme = sc.next();
        System.out.print("Geef het aantal processen in (5/10000/20000/50000):");
        aantalProcessen = sc.nextInt();

        initialiseerArrays();
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, arrivaltime, servicetime);

        switch (algoritme) {
            case "FCFS" -> berekenFCFS();
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
        arrivaltime = new int[aantalProcessen];
        servicetime = new int[aantalProcessen];
        remainingtime = new int[aantalProcessen];
        starttime = new int[aantalProcessen];
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
        avgnormalizedturnaroundtime += (double) turnaroundtime[i]/servicetime[i];
    }
    private static boolean stillRemainingTime() {
        int remainingTime=0;
        for (int i=0; i<aantalProcessen; i++){
            remainingTime += remainingtime[i];
        }
        return remainingTime > 0;
    }
    private static boolean stillUnfinishedProcesses() {
        int aantalUnfinished=0;
        for (int i=0; i<aantalProcessen-1; i++){
            if (endtime[i] == 0) aantalUnfinished++;
        }
        return aantalUnfinished > 0;
    }
    private static void printResultaten() {
        System.out.println("\npid  arrival  service  start   end turnaround wait - time ");
        for(int  i = 0; i< aantalProcessen;  i++) {
            System.out.println(pid[i] + "\t\t" + arrivaltime[i] + "\t\t" + servicetime[i] + "\t\t" + starttime[i]+ "\t\t" + endtime[i] + "\t\t" + turnaroundtime[i] + "\t\t"  + waittime[i] ) ;
        }
        System.out.print("\naverage turnaround time: "+ (avgturnaroundtime/aantalProcessen));
        System.out.print("\naverage normalized turnaround time: "+ (avgnormalizedturnaroundtime/aantalProcessen));
        System.out.print("\naverage wait time: "+ (avgwaittime/aantalProcessen));
    }




    // -------------------- First Come First Serve (FCFS) --------------------
    static void berekenFCFS() {
        for(int i = 0 ; i < aantalProcessen; i++) {
            if( i == 0 || arrivaltime[i] > endtime[i-1] ) {
                starttime[i] = arrivaltime[i];
                endtime[i] = arrivaltime[i] + servicetime[i];
            }
            else {
                starttime[i] = endtime[i-1];
                endtime[i] = starttime[i] + servicetime[i];
            }
            berekenGevraagde(i);
        }
    }





    // -------------------- Shortest Remaining Time (SRT) --------------------
    static void berekenSRT() {
        System.arraycopy(servicetime, 0, remainingtime, 0, aantalProcessen);
        int processWithSRT;
        huidigeTijd=0;
        while (stillRemainingTime()){
            processWithSRT = kleinsteTijdSRT();
            if (processWithSRT>-1) {
                if (starttime[processWithSRT] == 0 && arrivaltime[processWithSRT] != 0) starttime[processWithSRT] = huidigeTijd;
                remainingtime[processWithSRT]--;
                if (remainingtime[processWithSRT] == 0)
                    endtime[processWithSRT] = huidigeTijd + 1;
            }
            huidigeTijd++;
        }
        for (int i = 0; i<aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }
    private static int kleinsteTijdSRT() {
        int processWithSRT = -1;
        int min = Integer.MIN_VALUE-8;
        for (int i = 0; i<aantalProcessen; i++)
            if (arrivaltime[i]<=huidigeTijd && remainingtime[i]<min && remainingtime[i] != 0) {
                min = remainingtime[i];
                processWithSRT = i;
            }
        return processWithSRT;
    }





    // -------------------- Highest Response Ratio Next (HRRN) --------------------
    static void berekenHRRN() {
        int processWithHRRN;
        huidigeTijd=0;
        while (stillUnfinishedProcesses()){
            processWithHRRN = grootsteTijdHRRN();
            if(processWithHRRN>-1) {
                endtime[processWithHRRN] = huidigeTijd + servicetime[processWithHRRN];
                starttime[processWithHRRN] = endtime[processWithHRRN] -servicetime[processWithHRRN];
                huidigeTijd += servicetime[processWithHRRN];
            }
            else
                huidigeTijd++;
        }
        for (int i = 0; i<aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }
    private static int grootsteTijdHRRN() {
        int processWithHRRN = -1;
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






    // -------------------- Round Robin (RR) --------------------
    static void berekenRR(int timeslice) {
        System.arraycopy(servicetime, 0, remainingtime, 0, aantalProcessen);
        huidigeTijd = 0;
        vulNogToekomendeProcesses();
        int process = -1;
        while (stillRemainingTime()) {
            checkOpToekomendeProcessesRR();
            if ( process != -1 && remainingtime[process] > 0)
                readyqueue.add(process);
            process = schedulingProcessFromReadyQueueRR(timeslice);
        }
        for (int i = 0; i < aantalProcessen; i++) {
            berekenGevraagde(i);
        }
    }
    private static void vulNogToekomendeProcesses() {
        readyqueue = new LinkedList<>();
        nogToekomendeProcesses = new LinkedList<>();
        for (int i = 0; i<aantalProcessen; i++)
            nogToekomendeProcesses.add(i);
    }
    private static void checkOpToekomendeProcessesRR() {
        int toekomendprocess;
        while (!nogToekomendeProcesses.isEmpty() && arrivaltime[nogToekomendeProcesses.peek()] == huidigeTijd) {
            toekomendprocess = nogToekomendeProcesses.remove();
            readyqueue.add(toekomendprocess);
            starttime[toekomendprocess] = huidigeTijd;
        }
    }
    private static int schedulingProcessFromReadyQueueRR(int timeslice) {
        if (readyqueue.isEmpty())
            huidigeTijd++;
        else {
            int processWithRR = readyqueue.remove();
            if (remainingtime[processWithRR] > timeslice) {
                for (int i = 0; i<timeslice; i++) {
                    checkOpToekomendeProcessesRR();
                    huidigeTijd++;
                }
                remainingtime[processWithRR] -= timeslice;
                return processWithRR;
            } else if (remainingtime[processWithRR] > 0 && remainingtime[processWithRR] <= timeslice ) {
                for (int i = 0; i<remainingtime[processWithRR]; i++) {
                    checkOpToekomendeProcessesRR();
                    huidigeTijd++;
                }
                endtime[processWithRR] = huidigeTijd;
                remainingtime[processWithRR] = 0;
            }
        }
        return -1;
    }





    // -------------------- MultiLevel FeedBack (MLFB) --------------------
    static void berekenMLFB(int versie){
        maakQueues();
        System.arraycopy(servicetime, 0, remainingtime, 0, aantalProcessen);
        huidigeTijd = 0;
        vulNogToekomendeProcesses();
        if (versie == 1) {
            while (stillRemainingTime()) {
                checkOpToekomendeProcessesMLFB();
                schedulingProcessesMLFBv1();
            }
        }
        else if (versie == 2) {
            while (stillRemainingTime()) {
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
            if (remainingtime[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue2.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue2.isEmpty()) {
            processWithMLFB = queue2.remove();
            if (remainingtime[processWithMLFB] > 2) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue3.add(processWithMLFB);
            } else if (remainingtime[processWithMLFB] > 0) {
                for (int i = 0; i<remainingtime[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue3.isEmpty()) {
            processWithMLFB = queue3.remove();
            if (remainingtime[processWithMLFB] > 4) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue4.add(processWithMLFB);
            } else if (remainingtime[processWithMLFB] > 0 ) {
                for (int i = 0; i<remainingtime[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue4.isEmpty()) {
            processWithMLFB = queue4.remove();
            if (remainingtime[processWithMLFB] > 8) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else if (remainingtime[processWithMLFB] > 0) {
                for (int i = 0; i<remainingtime[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue5.isEmpty()) {
            processWithMLFB = queue5.remove();
            if (remainingtime[processWithMLFB] > 16) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else if (remainingtime[processWithMLFB] > 0) {
                for (int i = 0; i<remainingtime[processWithMLFB]; i++) {
                    checkOpToekomendeProcessesMLFB();
                    huidigeTijd++;
                }
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
    }

    private static void schedulingProcessesMLFBv1() {
        int processWithMLFB = -1;
        if (!queue1.isEmpty()) {
            processWithMLFB = queue1.remove();
            if (remainingtime[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue2.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue2.isEmpty()) {
            processWithMLFB = queue2.remove();
            if (remainingtime[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue3.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue3.isEmpty()) {
            processWithMLFB = queue3.remove();
            if (remainingtime[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue4.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue4.isEmpty()) {
            processWithMLFB = queue4.remove();
            if (remainingtime[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
        else if (!queue5.isEmpty()) {
            processWithMLFB = queue5.remove();
            if (remainingtime[processWithMLFB] > 1) {
                remainingtimehigherthentimeslice(processWithMLFB);
                queue5.add(processWithMLFB);
            } else {
                checkOpToekomendeProcessesMLFB();
                huidigeTijd++;
                endtime[processWithMLFB] = huidigeTijd;
                remainingtime[processWithMLFB] = 0;
            }
        }
    }

    private static void remainingtimehigherthentimeslice(int process) {
        huidigeTijd++;
        checkOpToekomendeProcessesMLFB();
        remainingtime[process]--;
    }

    private static void checkOpToekomendeProcessesMLFB() {
        int toekomendprocess;
        while (!nogToekomendeProcesses.isEmpty() && arrivaltime[nogToekomendeProcesses.peek()] == huidigeTijd) {
            toekomendprocess = nogToekomendeProcesses.remove();
            queue1.add(toekomendprocess);
            starttime[toekomendprocess] = huidigeTijd;
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
