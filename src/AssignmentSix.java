import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


/**
 * This application... 
 * 
 * @author Team 6: Jared Cheney, Andrew Meraz, Chul Kim and Agustin Garcia
 *
 */
public class AssignmentSix
{
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS];
   
   public static void main(String[] args)
   {
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      
      CardGameFramework highCardGame = new CardGameFramework( 
            numPacksPerDeck, numJokersPerPack,  
            numUnusedCardsPerPack, unusedCardsPerPack, 
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      
      //We have two hands from the highCardGame object.
      //calling deal to deal all of the cards per each hand.  (7 cards per hand)
      highCardGame.deal();
      
      // establish main frame in which program will run
      CardTable myCardTable 
         = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 700);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // set up layout which will control placement of buttons, etc.
      GridLayout layout = new GridLayout(4, 1);
      myCardTable.setLayout(layout);

      Hand computerHand = highCardGame.getHand(0);
      Hand yourHand = highCardGame.getHand(1);
      
      //HighCard Game
      
      // Create a Playing Area Hand
      JPanel pnlPlayArea = new JPanel();
      Border border = new TitledBorder("Playing Area");
      pnlPlayArea.setBorder(border);
      
      GridLayout gridLayout = new GridLayout(2, 2);
      pnlPlayArea.setLayout(gridLayout);

      JPanel pnlComputerArea = new JPanel();
      JPanel pnlYourHandArea = new JPanel();
      
      createHandJLabels(myCardTable, computerHand, computerHand, computerLabels, true, pnlPlayArea, pnlComputerArea,
            pnlYourHandArea, pnlPlayArea);
      createHandJLabels(myCardTable, yourHand, computerHand, humanLabels, false, pnlPlayArea, pnlComputerArea,
            pnlYourHandArea, pnlPlayArea);

      displayHandArea(myCardTable, "Computer Hand", computerLabels, pnlComputerArea);

      displayPlayingArea(pnlPlayArea, pnlPlayArea, myCardTable, null, null);

      // My Hand
      displayHandArea(myCardTable, "Your Hand", humanLabels, pnlYourHandArea);
      
      
      /************************************************************************
       * TIMER CODE  
       */
      JPanel timerArea = new JPanel();
      JLabel label = new JLabel();
      Timer timer = new Timer(label);
      JButton btn = new JButton("PLAY");
      label.setFont(new Font("Serif", Font.PLAIN, 28));
      label.setForeground(Color.RED);
      timerArea.setBorder(new TitledBorder(""));
      timerArea.add(label);
      timerArea.add(btn);
      myCardTable.add(timerArea);
      
      btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) { 
            if (e.getActionCommand().equals("PLAY")) {
               if (timer.getFormattedTime().equals("00:00")) {
                  timer.start();
               } else {
                  timer.resumeAction(); 
               }
               btn.setText("PAUSE");
            } else {
               timer.pauseAction();
               btn.setText("PLAY");
            }
         }          
      });   
      /*
       * END TIMER CODE
       ***********************************************************************/

      // show everything to the user
      myCardTable.setVisible(true);
   }
   
   /**
    * 
    * @param myCardTable
    * @param hand
    * @param JLabels
    * @param isBackCard
    */
   private static void createHandJLabels(CardTable myCardTable, Hand hand, Hand computerHand, JLabel[] JLabels,
         boolean isBackCard, JPanel pnlPlayArea, JPanel pnlComputerArea, JPanel pnlYourHandArea, JPanel pnlPlayAreaPosition) {
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
         // Create an icon and store in an array later use.
         Icon icon = null;
         HighCardListener highCardListener = null;
         Card dealCard = hand.inspectCard(i);
         if (isBackCard) {
            icon = GUICard.getBackCardIcon();
         } else {
            icon = GUICard.getIcon(dealCard);
            highCardListener = new HighCardListener(myCardTable, dealCard, hand, computerHand, pnlPlayArea,
                  pnlComputerArea, pnlYourHandArea, pnlPlayAreaPosition);
         }
         JLabel jlabel = new JLabel(icon);
         if (highCardListener != null) {
            jlabel.addMouseListener(highCardListener);
         }
         JLabels[i] = jlabel;
      }
   }

   /**
    * Displays your hand.
    * @param myCardTable
    * @param yourHand
    */
   private static void displayHandArea(CardTable myCardTable, String areaTitle, JLabel[] JLables, JPanel pnlHand) {
      // My Hand
      Border border = new TitledBorder(areaTitle);
      pnlHand.setBorder(border);
      for (int i = 0; i < NUM_CARDS_PER_HAND; i ++) {
         pnlHand.add(JLables[i]);
      }
      GridLayout layout = new GridLayout(1, NUM_CARDS_PER_HAND);
      pnlHand.setLayout(layout);
      myCardTable.add(pnlHand);
   }
   

   /**
    * 
    * @param myCardTable
    * @param computerCard
    * @param yourCard
    * @return
    */
   private static void displayPlayingArea(JPanel pnlPlayArea, JPanel pnlPlayAreaPosition, CardTable myCardTable, Card computerCard, Card yourCard ) {

      //Play Computer Card
      JLabel computerCardJLabel = null;
      if (computerCard != null) {
         Icon computerCardIcon = GUICard.getIcon(computerCard);
         computerCardJLabel = new JLabel(computerCardIcon);
         playedCardLabels[0] = computerCardJLabel;
      }
      JLabel computerLabel = new JLabel( "Computer", JLabel.CENTER );
      playLabelText[0] = computerLabel;

      //Play Your Card
      JLabel yourCardJLabel = null;
      if (yourCard != null) {
         Icon yourCardIcon = GUICard.getIcon(yourCard);
         yourCardJLabel = new JLabel(yourCardIcon);
         playedCardLabels[1] = yourCardJLabel;
      }
      JLabel yourHandLabel = new JLabel( "You", JLabel.CENTER );
      playLabelText[1] = yourHandLabel;

      //Add labels to the play area
      
      if (computerCardJLabel != null) {
         pnlPlayArea.add(computerCardJLabel);
      }
      if (yourCardJLabel != null) {
         pnlPlayArea.add(yourCardJLabel);
      }
      
      pnlPlayAreaPosition.add(computerLabel);
      pnlPlayAreaPosition.add(yourHandLabel);

      myCardTable.add(pnlPlayArea);
      myCardTable.add(pnlPlayAreaPosition);
   }
   

   private static class CardTable extends JFrame
   {
      static int MAX_CARDS_PER_HAND = 56;
      static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games    
      private int numCardsPerHand;
      private int numPlayers;
      public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
      
      public CardTable(String title, int numCardsPerHand, int numPlayers)
      {
         if (numCardsPerHand > MAX_CARDS_PER_HAND) {
            throw new IllegalArgumentException("Max number of cards per hand:" + MAX_CARDS_PER_HAND);
         }
         if (numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException("Max number of players are:" + MAX_PLAYERS);
         }
         this.numCardsPerHand = numCardsPerHand;
         this.numPlayers = numPlayers;
      }
      
      public int getNumCardsPerHand()
      {
         return numCardsPerHand;
      }
      
      public int getNumPlayers()
      {
         return numPlayers;
      }
   }
   
   /**
    * 
    * @author charlesk
    *
    */
   private static class HighCardListener implements MouseListener {
      private CardTable myCardTable;
      private Card card;
      private Hand hand;
      private Hand computerHand;
      private JPanel pnlPlayArea;
      private JPanel pnlComputerArea;
      private JPanel pnlYourHandArea;
      private JPanel pnlPlayAreaPosition;
      
      HighCardListener(CardTable myCardTable, Card card, Hand hand, Hand computerHand, JPanel pnlPlayArea,
            JPanel pnlComputerArea, JPanel pnlYourHandArea, JPanel pnlPlayAreaPosition) {
         this.myCardTable = myCardTable;
         this.card = card;
         this.hand = hand;
         this.computerHand = computerHand;
         this.pnlPlayArea = pnlPlayArea;
         this.pnlComputerArea = pnlComputerArea;
         this.pnlYourHandArea = pnlYourHandArea;
         this.pnlPlayAreaPosition = pnlPlayAreaPosition;
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        pnlPlayAreaPosition.removeAll();
          pnlPlayArea.removeAll();

          // Play the card to the playing are when clicked.
          //1) Add the card to the playing area
          Icon icon = GUICard.getIcon(card);
          JLabel iconYourHandJLabel = new JLabel(icon);
          playedCardLabels[0] = iconYourHandJLabel;
          
          //2) remove the card from the hand and hand area.
          for (int i = 0; i < hand.getNumCards(); i ++) {
             Card card = hand.inspectCard(i);
             if (card.getValue() == this.card.getValue() && card.getSuit() == this.card.getSuit()) {
                hand.playCard(i);
                break;
             }
          }
          pnlYourHandArea.remove(e.getComponent());
          pnlYourHandArea.revalidate();
          
          //3) Randomly pick and add the card from the computer hand and remove it from the hand as well.
          int computerHandCards = this.computerHand.getNumCards() - 1;
          Random r = new Random();
          int cCard = r.nextInt((computerHandCards - 0) + 1);

          Card computerCard = computerHand.playCard(cCard);
          icon = GUICard.getIcon(computerCard);
          JLabel iconComputerJLabel = new JLabel(icon);
          playedCardLabels[1] = iconComputerJLabel;
          pnlComputerArea.remove(cCard);
          pnlComputerArea.revalidate();
          
          //Card labels
          pnlPlayArea.add(iconComputerJLabel);
          pnlPlayArea.add(iconYourHandJLabel);
          //Text labels
          JLabel computerLabel = new JLabel( "Computer", JLabel.CENTER );
          JLabel yourHandLabel = new JLabel( "You", JLabel.CENTER );
          pnlPlayAreaPosition.add(computerLabel);
          pnlPlayAreaPosition.add(yourHandLabel);

          //Refresh the JFrame
          myCardTable.revalidate();

          int yourCardValue = this.card.getValueAsInt();
          int computerCardValue = computerCard.getValueAsInt();
          String highCardResultMessage = "";
          
          //Determine who won
          if (this.card.getValue() == 'A' && computerCard.getValue() != 'A') {
             highCardResultMessage = "You Won with Ace!";
          } else if (computerCard.getValue() == 'A' && this.card.getValue() != 'A') {
             highCardResultMessage = "You Lost to an Ace!";
          } else if (computerCard.getValue() == 'A' && this.card.getValue() == 'A') {
             highCardResultMessage = "Draw with an Ace!";
          }
          else {
             if (yourCardValue > computerCardValue) {
                highCardResultMessage = "You Won!";
             } else if (yourCardValue == computerCardValue) {
                highCardResultMessage = "Draw!";
             } else {
                highCardResultMessage = "Sorry You Lost!";
             }
          }
          JOptionPane.showMessageDialog(myCardTable, highCardResultMessage);

          //Clean up the playing area
          pnlPlayArea.removeAll();
          myCardTable.revalidate();
          myCardTable.repaint();
      }

      @Override
      public void mousePressed(MouseEvent e) {
         // TODO Auto-generated method stub
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        
      }

      @Override
      public void mouseEntered(MouseEvent e) {
         // TODO Auto-generated method stub
      }

      @Override
      public void mouseExited(MouseEvent e) {
         // TODO Auto-generated method stub
      }
   }
   /**
    *  It will read the image files and store them in a static Icon array. 
    *  Rather than a 1-D array of Phase 1, this will be a 2-D array to facilitate addressing the value and suit of a Card in order get its Icon.
    *  
    *  We have to be able to convert from chars and suits to ints, and back again, in order to find the Icon for any given Card object. 
    * @author charlesk
    *
    */
   private static class GUICard
   {
      public static final String IMAGE_FOLDER_NAME = "images";
      public static final String BACK_IMAGE_NAME = "BK.gif";

      public static final int MAX_ROW = 14;
      public static final int MAX_COLUMN = 4;
      
      // 14 = A thru K + joker
      //The 52 + 4 jokers Icons will be read and stored into the iconCards[][] array.
      private static Icon[][] iconCards = new ImageIcon[MAX_ROW][MAX_COLUMN];
      
      //The card-back image in the iconBack member
      private static Icon iconBack;
      
      static boolean iconsLoaded = false;
      
      //
      static {
         loadCardIcons();
      }
      
      /**
       *  Icons are loaded following rules
       *  C = Clover, D = Diamond, H = Hearts S = Spades
       *  
       *  [A,C],[A,D],[A,H],[A,S]
       *  [2,C],[2,D],[2,H],[2,S]
       *  [3,C],[3,D],[3,H],[3,S]
       *  ...
       *  [J,C],[J,D],[J,H],[J,S]
       *  ...
       *  [K,C],[K,D],[K,H],[K,S]
       */
      public static void loadCardIcons()
      {
         if (iconsLoaded ) {
            return;
         }
         //Load iconBack image
         iconBack= new ImageIcon(IMAGE_FOLDER_NAME + "/" +  BACK_IMAGE_NAME);
         
         //Load other icons(cards)
         for (int r = 0; r < MAX_ROW; r ++) {
            for (int c = 0; c < MAX_COLUMN; c ++) {
               iconCards[r][c] =  new ImageIcon(IMAGE_FOLDER_NAME + "/" +  getImageFileName(r, c));
            }
         }
 
         iconsLoaded = true;
      }
      
      private static String getImageFileName(int row, int column) {
         StringBuilder ret = new StringBuilder();
         switch(row) {
         case 0:
            ret.append('A');
            break;
         case 1:
            ret.append('2');
            break;
         case 2:
            ret.append('3');
            break;
         case 3:
            ret.append('4');
            break;
         case 4:
            ret.append('5');
            break;
         case 5:
            ret.append('6');
            break;
         case 6:
            ret.append('7');
            break;
         case 7:
            ret.append('8');
            break;
         case 8:
            ret.append('9');
            break;
         case 9:
            ret.append('T');
            break;
         case 10:
            ret.append('J');
            break;
         case 11:
            ret.append('Q');
            break;
         case 12:
            ret.append('K');
            break;
         case 13:
            ret.append('X');
            break;
         default:
            throw new IllegalArgumentException("Illegal value of row");
         }
         
         switch(column) {
         case 0:
            ret.append('S');
            break;
         case 1:
            ret.append('H');
            break;
         case 2:
            ret.append('D');
            break;
         case 3:
            ret.append('C');
            break;
         default:
            throw new IllegalArgumentException("Illegal value of column");
         }
         
         ret.append(".gif");
         return ret.toString();
      }
      /**
       * 
       * @param card
       * @return the Icon for that card
       */
      public static Icon getIcon(Card card)
      {
         return iconCards[valueAsInt(card)][suitAsInt(card)];
      }
      
      /**
       * 
       * @param card
       * @return
       */
      private static int valueAsInt(Card card) {
         int ret = 0;
         char valueChar = card.getValue();
         switch(valueChar) {
         case 'A':
            ret = 0;
            break;
         case '2':
            ret = 1;
            break;
         case '3':
            ret = 2;
            break;
         case '4':
            ret = 3;
            break;
         case '5':
            ret = 4;
            break;
         case '6':
            ret = 5;
            break;
         case '7':
            ret = 6;
            break;
         case '8':
            ret = 7;
            break;
         case '9':
            ret = 8;
            break;
         case 'T':
            ret = 9;
            break;
         case 'J':
            ret = 10;
            break;
         case 'Q':
            ret = 11;
            break;
         case 'K':
            ret = 12;
            break;
         case 'X':
            ret = 13;
            break;
         default:
            throw new IllegalArgumentException("Illegal value of card value.");
      }
         return ret;
      }
      
      /**
       * 
       * @param card
       * @return
       */
      private static int suitAsInt(Card card) {
         int ret = 0;
         Card.Suit suit = card.getSuit();
         switch(suit) {
         case spades:
            ret = 0;
            break;
         case hearts:
            ret = 1;
            break;
         case diamonds:
            ret = 2;
            break;
         case clubs:
            ret = 3;
            break;
         default:
            throw new IllegalArgumentException("Illegal value of card suit.");
         }
         return ret;
      }
      
      /**
       * 
       * @return back card icon
       */
      public static Icon getBackCardIcon()
      {
         return iconBack;
      }
   }
}

class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   // # standard 52-card packs per deck, ignoring jokers or unused cards
   private int numPacks;            
   // if 2 per pack & 3 packs per deck, get 6
   private int numJokersPerPack;
   // # cards removed from each pack
   private int numUnusedCardsPerPack;
   // # cards to deal each player
   private int numCardsPerHand;
   // holds the initial full deck and gets smaller (usually) during play
   private Deck deck;       
   // one Hand for each player
   private Hand[] hand;  
   // an array holding the cards not used in the game. e.g. pinochle does not
   // use cards 2-8 of any suit
   private Card[] unusedCardsPerPack;

   public CardGameFramework(int numPacks, int numJokersPerPack,
       int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
       int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6) {
         numPacks = 1;
      }
      
      if (numJokersPerPack < 0 || numJokersPerPack > 4) {
         numJokersPerPack = 0;
      }
      
      //  > 1 card
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) {
         numUnusedCardsPerPack = 0;
      }
      
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS) {
         numPlayers = 4;
      }
      
      // one of many ways to assure at least one full deal to all players
      if (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack) / numPlayers) {
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;
      }

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];

      for (k = 0; k < numPlayers; k++) {
         this.hand[k] = new Hand();
      }
      
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;

      for (k = 0; k < numUnusedCardsPerPack; k++) {
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
      }

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers) {
         return new Hand();
      }

      return hand[k];
   }

   public Card getCardFromDeck()
   {
      return deck.dealCard();
   }

   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++) {
         hand[k].resetHand();
      }

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++) {
         deck.removeCard( unusedCardsPerPack[k] );
      }

      // add jokers
      for (k = 0; k < numPacks; k++) {
         for ( j = 0; j < numJokersPerPack; j++) {
            deck.addCard( new Card('X', Card.Suit.values()[j]) );
         }
      }

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++) {
         hand[j].resetHand();
      }

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++) {
         for (j = 0; j < numPlayers; j++) {
            if (deck.getNumCards() > 0) {
               hand[j].takeCard( deck.dealCard() );
            } else {
               enoughCards = false;
               break;
            }
         }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++) {
         hand[k].sort();
      }
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
            cardIndex < 0 || cardIndex > numCardsPerHand - 1) {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);      
      }
 
      // return the card played
      return hand[playerIndex].playCard(cardIndex);
 
   }

   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1) {
         return false;
      }
   
      // Are there enough Cards?
      if (deck.getNumCards() <= 0) {
         return false;
      }

      return hand[playerIndex].takeCard(deck.dealCard());
   }
}

/*
 * A class that represents a playing card
 */
class Card
{
   private char value;
   private boolean errorFlag;
   private Suit suit;
   public enum Suit {clubs, diamonds, hearts, spades};

   public static char[] ORDERED_CARD_VALUES = new char[] 
         {'A','2','3','4','5','6','7','8','9','T','J','Q','K','X'};
   
   // Default card is 'A of spades'
   public Card()
   {
      this('A', Suit.spades);
   }

   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

   /*
    * Sets the value and suit of a Card if value and suit are valid.
    * If valid, set errorFlag to false.
    * If invalid, set errorFlag to true.
    * Returns true/false if the value and suit were set
    */
   public boolean set(char value, Suit suit)
   {
      boolean wasSet;

      if (isValid(value, suit)) {
         setValue(value);
         setSuit(suit);
         errorFlag = false;
         wasSet = true;
      } else {
         errorFlag = true;
         wasSet = false;
      }
      
      return wasSet;
   }

   /*
    * Returns a string representation of a Card object if valid
    * otherwise returns an error message
    */
   public String toString()
   {
      String message;

      if (!errorFlag) {
         message = String.format("%s of %s", value, suit);
      } else {
         message = "ERROR: Invalid card";
      }
      
      return message;
   }

   /*
    * Checks the equality of two Card objects.
    * Returns true if card value and card suit match
    * Returns false otherwise
    */
   public boolean equals(Card card)
   {
      boolean areEqual;
      
      if(value == card.value && suit == card.suit
            && errorFlag == card.errorFlag) {
         areEqual = true;
      } else {
         areEqual = false;
      }
      
      return areEqual;
   }

/*
    * Checks the validity of value and suit.
    * Valid values: A, 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, X (Joker)
    * Valid suits: spades, hearts, diamonds, clubs.
    * Note: suits are not validated at this time
    */
   private boolean isValid(char value, Suit suit)
   {
      boolean validCard;
      
      switch(value) {
         case 'A':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case 'T':
         case 'J':
         case 'Q':
         case 'K':
         case 'X':
            validCard = true;
            break;
         default:
            validCard =  false;
      }
      
      return validCard;
   }

   public Suit getSuit()
   {
      return suit;
   }

   public boolean setSuit(Suit suit)
   {
      this.suit = suit;
      return true;
   }

   public char getValue()
   {
      return value;
   }

   public int getValueAsInt() {
      int ret = 0;
      switch(value) {
      case 'A':
         ret = 0;
         break;
      case '2':
         ret = 1;
         break;
      case '3':
         ret = 2;
         break;
      case '4':
         ret = 3;
         break;
      case '5':
         ret = 4;
         break;
      case '6':
         ret = 5;
         break;
      case '7':
         ret = 6;
         break;
      case '8':
         ret = 7;
         break;
      case '9':
         ret = 8;
         break;
      case 'T':
         ret = 9;
         break;
      case 'J':
         ret = 10;
         break;
      case 'Q':
         ret = 11;
         break;
      case 'K':
         ret = 12;
         break;
      case 'X':
         ret = 13;
         break;
      default:
         throw new IllegalArgumentException("Illegal value of card value.");
   }
      return ret;
   }
   
   public int getSuitAsInt() {
      int ret = 0;
      switch(suit) {
      case spades:
         ret = 0;
         break;
      case hearts:
         ret = 1;
         break;
      case diamonds:
         ret = 2;
         break;
      case clubs:
         ret = 3;
         break;
      default:
         throw new IllegalArgumentException("Illegal value of card suit.");
      }
      return ret;
   }
   
   public void setValue(char value)
   {
      this.value = value;
   }

   public boolean isErrorFlag()
   {
      return errorFlag;
   }
   
   /**
    * 
    * @return ordered card values
    */
   public static char[] valuRanks() {
      return ORDERED_CARD_VALUES;
   }
   
   /**
    * Sort the incoming array of cards using a bubble sort routine.
    * Sort by Value and suit. The suits are ordered as follows: spades, hearts, diamonds, clubs.
    * @param cards
    */
   public static void arraySort(Card[] cards) {
      int n = cards.length;
      // Sort by Value first.
      for (int i = 0; i < n - 1; i++) {
         for (int j = 0; j < n - i - 1; j++) {
            if (cards[j].getValueAsInt() > cards[j + 1].getValueAsInt()) {
               // swap cards[j+1] and cards[i]
               Card temp = cards[j];
               cards[j] = cards[j + 1];
               cards[j + 1] = temp;
            }
         }
      }
      // Sort by Suit
      for (int i = 0; i < n - 1; i++) {
         for (int j = 0; j < n - i - 1; j++) {
            if (cards[j].getSuitAsInt() > cards[j + 1].getSuitAsInt()) {
               // swap cards[j+1] and cards[i]
               Card temp = cards[j];
               cards[j] = cards[j + 1];
               cards[j + 1] = temp;
            }
         }
      }
   }
}

/*
 * A class that represents the source of the cards for dealing and,
 * as the game progresses, the place from which players can receive new cards
 */
class Deck
{
   public static final int MAX_CARDS = 6 * 56;
   private static Card[] masterPack = new Card[56];
   private Card[] cards;
   private int topCard;
   
   //Overload the deck class; default only 1 class
   public Deck ()
   {
      this(1);
   }
   
   //Deck class with number of packs as the argument
   public Deck(int numPacks)
   {
      allocateMasterPack();

      //Fatal error if more than 6 decks used. 
      if (numPacks * 56 > MAX_CARDS) {
         System.out.println("Fatal Error. Too many decks. Need "
               + "count between 1 and 6");
         System.exit(0);
      }

      this.cards = new Card[56 * numPacks];
      init(numPacks);
   }
   
   //Initiates the cards array from the masterPack
   public void init(int numPacks)
   {
      topCard = 56 * numPacks - 1;
      int x = 0;

      for(int i = 0; i < numPacks; i++) {
         for(int j = 0; j < 56; j++) {
            cards[x++] = masterPack[j];
         }
      }
   }
   
   //Shuffle changes the position of the cards
   public void shuffle()
   {
      int rand;
      Card temp;

      for(int i = 0; i < cards.length; i++) {
         temp = cards[i]; //create a temp variable for the current card
         //Math.random is used to get a random card from the array to switch
         //with the current card.
         rand = (int)(Math.random() * (cards.length - 1));
         this.cards[i] = this.cards[rand];
         this.cards[rand] = temp;
         
      }
   }
   
   //returns the top card in the deck and removes 1 from topCard
   public Card dealCard()
   {
      return this.cards[topCard--];
   }
   
   //selects the top card index
   public int getTopCard()
   {
      return topCard;
   }
   
   //inspects the card to see if it is valid within the deck
   public Card inspectCard(int k)
   {
      Card cardToInspect;

      if(this.cards == null || this.cards.length == 0
            || k > this.cards.length) {
         Card errorCard = new Card();
         errorCard.setValue('E');
         cardToInspect = errorCard; 
      } else {
         cardToInspect = this.cards[k];
      }

      return cardToInspect;
   }
   
   //creates the master pack of 56 cards that does not change
   private static void allocateMasterPack()
   {
      int k = 0; //used to count the index of the masterPack deck.
      Card.Suit[] suits = Card.Suit.values();
      char[] cardValues = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};

      //if null then create new deck
      if(masterPack[51] == null) {
         for(Card.Suit suit : suits) {
            for(char value : cardValues) {
               masterPack[k++] = new Card(value, suit);
            }
         }
      }
   }
   
   // make sure that there are not too many instances of the card in the deck if you add it.
   // Return false if there will be too many.  It should put
   // the card on the top of the deck.
   public boolean addCard(Card card)
   {
      if (this.cards == null || this.topCard == this.cards.length) {
         return false;
      }
      this.cards[topCard++] = card;
      return true;
   }
   
   // you are looking to remove a specific card from the deck.  Put the
   // current top card into its place.  Be sure the card you need is actually
   // still in the deck, if not return false.
   public boolean removeCard(Card card)
   {
      if (this.cards == null || this.cards.length == 0) {
         return false;
      }
      boolean exists = false;
      for (int i = 0; i < this.cards.length; i ++) {
         Card cardInDeck = this.cards[i];
         if (cardInDeck.getValue() == card.getValue() &&
               cardInDeck.getSuit() == card.getSuit()) {
            //Found the matching card. Remove it and put the current top card into its place
            exists = true;
            this.cards[i] = this.cards[topCard];
            this.cards[topCard] = null;
            topCard--;
         }
      }
      return exists;
   }
   
   /**
    * put all of the cards in the deck back into the right order according to their values and suits. 
    */
   public void sort()
   {
      Card.arraySort(this.cards);
   }
   
   // return the number of cards remaining in the deck.
   public int getNumCards()
   {
      return this.cards.length;
   }
}

/**
 * A class that represents the cards held by a single player.
 * 
 * Hand object usually contains several cards, so we'll need an array of Card objects (myArray) as the principal member of the Hand class.  
 * Since each game deals a different number of cards into its players hands, and even within a game the number of cards in a hand will increase or decrease,
 * we must keep track of this with an int value (numCards).  
 * 
 * We'll need constructors, mutators, etc., of course.  
 * 
 * We'll also want a way for the hand to receive a card (from the deck or somewhere else), 
 * 
 * and play a card (to the table or to another player).  
 * 
 * These two methods will be called takeCard() and playCard(), respectively. 
 * 
 * Since this class has no information about the game being played, it always puts new cards received by takeCard() into the next available location of the array (index position numCards) 
 * 
 * and plays a card via playCard() from the highest occupied location (index position numCards - 1).  
 * 
 * The client game application would somehow prepare this highest position with the correct card to be played before calling Hand's playCard() method. 
 * 
 *  This detail is not our concern.
 * @author charlesk
 *
 */
class Hand
{
    public static final int MAX_CARDS = 100;
    private Card[] myCards;
    private int numCards;
    
    public Hand() {
       //create a default array of Cards
       this.myCards = new Card[MAX_CARDS];
    }
    
    /**
     * 
     * @return a number of cards
     */
    public int getNumCards() {
       return numCards;
    }

    /**
     * 
     * @param numCards
     */
    public boolean setNumCards(int numCards) {
       this.numCards = numCards;
       return true;
    }

    /**
     * remove all cards from the hand (in the simplest way).
     */
    public void resetHand() {
       //nothing to reset as myCards is either null or it's reset already.
       if (this.myCards == null || this.myCards.length == 0) {
          return;
       }
     
       this.numCards = 0;
       this.myCards = new Card[MAX_CARDS];
    }
    
    /**
     * adds a card to the next available position in the myCards array.
     * This is an object copy, not a reference copy, since the source of the Card might destroy or change 
     * its data after our Hand gets it -- we want our local data to be exactly as it was when we received it.
     * @param card
     * @return true if card was successfully added to the myCards array, otherwise return false.
     */
    public boolean takeCard(Card card) {
       boolean cardWasTaken;
 
       if (this.numCards == this.myCards.length) {
          cardWasTaken = false;
       } else {
          this.myCards[numCards++] = new Card(card.getValue(), card.getSuit());
          cardWasTaken = true;  
       }
        
       return cardWasTaken;
    }
    
    /**
     * 
     * @return Card and removes the card in the top occupied position of the array.
     */
    public Card playCard() {
       Card card = this.myCards[--numCards];
       this.myCards[numCards] = null;
       return card; 
    }

    /**
     * 
     */
    @Override
    public String toString() {
       StringBuilder myCards = new StringBuilder();
  
       for (int i = 0; i < this.numCards; i ++) {
          myCards.append(this.myCards[i]).append(" ");
       }
  
       return myCards.toString();
    }
    
    /**
     * Accessor for an individual card.
     * 
     * @param k
     * @return a card with errorFlag = true if k is bad.
     */
    public Card inspectCard(int k) {
       Card cardToInspect = null;
  
       if (this.myCards == null || this.myCards.length == 0 || k > this.myCards.length) {
          Card errorCard = new Card();
          errorCard.setValue('E');
          cardToInspect =  errorCard;
       } else {
         cardToInspect = this.myCards[k];
       }
  
       return cardToInspect;
    }
    
    /**
     * 
     * @param cardIndex
     * @return
     */
    public Card playCard(int cardIndex)
    {
       if ( numCards == 0 ) {
          //Creates a card that does not work
          return new Card('M', Card.Suit.spades);
       }
       
       //Decreases numCards.
       Card card = myCards[cardIndex];
       
       numCards--;
       for(int i = cardIndex; i < numCards; i++) {
          myCards[i] = myCards[i+1];
       }
       
       myCards[numCards] = null;
       
       return card;
    }
    
    /**
     * will sort the hand by calling the arraySort() method in the Card class
     */
    public void sort()
    {
       Card.arraySort(this.myCards);
    }
}

class Timer extends Thread
{
   private int timer;
   private boolean isPaused;
   private boolean isAlive;
   String formattedTime;
   JLabel label;
   
   public Timer(JLabel label) {
      timer = 0;
      isAlive = true;
      this.label = label;
      this.label.setText(getFormattedTime());
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
            label.setText(getFormattedTime());
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
   
   public String getFormattedTime() {
      if (timer < 3600) {
         int mins = Math.floorDiv(timer, 60);
         int secs = timer % 60;
         String minutes = (mins < 10) ? "0" + mins : mins + "";
         String seconds = (secs < 10) ? "0" + secs : secs + "";
         formattedTime = minutes + ":" + seconds;
      } else {
         formattedTime = "Over an hour";
      }

      return formattedTime;
   }
}