import java.util.Scanner;
/**
 * This application... 
 * 
 * @author Team 6: Jared Cheney, Andrew Meraz, Chul Kim and Agustin Garcia
 *
 */
public class AssignmentSix
{

   public static void main(String[] args)
   {
      Timer timer = new Timer();
      timer.start();
      Scanner keyboard = new Scanner(System.in);
      String command = keyboard.nextLine();
      while(!command.equals("stop")) {
         
         // pause button
         if (command.equals("pause")) {
            System.out.println("####### PAUSED ########");
            timer.pauseAction();
         }
         
         // play button
         if (command.equals("resume")) {
            System.out.println("####### RESUMED ########");
            timer.resumeAction();
         }

         command = keyboard.nextLine();
      }
      
      // when the game ends
      System.out.println("Timer ended");
      timer.destroy();
   }

}

class Timer extends Thread
{
   private int timer;
   private boolean isPaused;
   private boolean isAlive;
   
   public Timer() {
      timer = 0;
      isAlive = true;
   }
     
   @Override
   public void run() {
      while(isAlive){
         try {
            waitUntilResumed();
            doNothing();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         
         if (!isPaused) {
            System.out.println("Timer: " + timer);
            increment();  
         }
      }
   }
   
   private synchronized void increment() {
      timer++;
   }
   
   private synchronized void doNothing() throws InterruptedException {
      sleep(1000);
   }
   
   private synchronized void waitUntilResumed() throws InterruptedException {
      while (isPaused) {
         wait();
      }
   }
   
   public void pauseAction() {
      isPaused = true;
   }

   public synchronized void resumeAction() {
      isPaused = false; 
      notifyAll();
   }
   
   
   public void destroy() {
      isAlive = false;
   }
   
   public int getTimerValue() {
      return timer;
   }
}
