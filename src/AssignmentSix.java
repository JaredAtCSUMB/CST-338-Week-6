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
   }

}

// TODO: need to handle pause and resume
// Thread has wait() and notify()
class Timer extends Thread
{
   private int timer;
   
   public Timer() {
      timer = 0;
   }
     
   @Override
   public void run() {
      while(timer < 11){
         try {
             doNothing();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         System.out.println("Timer: " + timer);
         increment();
      }
      System.out.println("Timer ended");
   }
   
   private synchronized void increment() {
      timer++;
   }
   
   private synchronized void doNothing() throws InterruptedException {
      sleep(1000);
   }
   
   public int getTimerValue() {
      return timer;
   }
}
