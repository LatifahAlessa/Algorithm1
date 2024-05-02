import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BruteForcePortfolioOptimization{
    static int[] optimalAllocation = {0,0,0}; //static array to keep the best allocation
    static double expectedProtfolioReturn; //static value to keep the maximam expected Protfolio Return
    static double portfolioRiskLevel; //static value to keep the risk level 
 
     public static void main(String[] args) {
      String fileName = "investment.txt"; 
      //Initialize arrays for 3 assets
      String[] assetIds = new String[3];
      double[] expectedReturns = new double[3];
      double[] riskLevels = new double[3];
      int[] units = new int[3]; //to keep the units(quantity)
      int totalInvestment = 0;
      double riskTolerance = 0;
      try { //reading input from file
         BufferedReader reader = new BufferedReader(new FileReader(fileName));
         String line;

         int index = 0;
         //Read asset data
         while ((line = reader.readLine()) != null && !line.startsWith("Total") && index < 3) {
             String[] parts = line.split(" : ");
             String id = parts[0];
             double expectedReturn = Double.parseDouble(parts[1]);
             double riskLevel = Double.parseDouble(parts[2]);
             int quantity = Integer.parseInt(parts[3]);

             //Store data in arrays
             assetIds[index] = id;
             expectedReturns[index] = expectedReturn;
             riskLevels[index] = riskLevel;
             units[index] = quantity;
             index++;
         }
         //Read total investment and risk tolerance level
         if (line != null && line.startsWith("Total")) {
             String[] parts = line.split(" ");
             totalInvestment = Integer.parseInt(parts[3]);
             line = reader.readLine(); //Read the next line for risk tolerance
             String[] riskParts = line.split(" ");
             riskTolerance = Double.parseDouble(riskParts[4]);
         }
         reader.close();
         } catch (IOException e) {
            e.printStackTrace();
         }         
         //calling the brute-force method
         bruteForceOptimizePortfolio(expectedReturns, riskLevels, units, totalInvestment, riskTolerance);
         
         //printing the results
         System.out.println("Optimal Allocation");
         for (int i =0; i< assetIds.length; i++)   {
             System.out.println(assetIds[i] +": "+optimalAllocation[i]+" units");
         }
         System.out.println("  Expected Protfolio Return: " + expectedProtfolioReturn);
         System.out.println("Portfolio Risk Level: " + portfolioRiskLevel);
     }
     
     public static void bruteForceOptimizePortfolio(double[] expectedReturns, double[] riskLevels,int [] units, int totalInvestment, double riskTolerance) {
       double risk = 0, expReturn=0; //variables to keep the risk and  expected return of every new allocation
       int k =0;
       for (int i =0; i<=units[0]; i++)  { //first loop for the first asset
          for (int j =0; j<=units[1]; j++)  { //second loop for the second asset
             if (i+j >totalInvestment)
                break;
                
             k = totalInvestment - (i+j); //to keep the quantity for the third asset
             if (k >units[2])
               break;
             risk = calculateRiskLevel(i,j,k,riskLevels, totalInvestment); //calling the method that calculate Risk Level of each allocation
             
             if (risk > riskTolerance) //comparing the risk level of this allocation to the risk given by the user
                continue;
                
             expReturn = calculateExpectedReturn (i, j, k,expectedReturns,totalInvestment); //calling the method that calculate Expected Return of each allocation
             
             if (expReturn > expectedProtfolioReturn)  { //comparing the expected protfolio return of this allocation to the one given by the user
                optimalAllocation[0] = i;
                optimalAllocation[1] = j;
                optimalAllocation[2] = k;
                expectedProtfolioReturn = expReturn;
                portfolioRiskLevel = risk;
             }
                 
          }    
       }  
     }
     
     public static double calculateRiskLevel(int i, int j, int k, double[] riskLevels,int totalInvestment)   {
       return (((i*riskLevels[0])+(j*riskLevels[1])+(k*riskLevels[2]))/totalInvestment);
     }
     
     public static double calculateExpectedReturn(int i, int j, int k, double[] expectedReturns, int totalInvestment)   {
       return (((i*expectedReturns[0])+(j*expectedReturns[1])+(k*expectedReturns[2]))/totalInvestment);
     }     
 }