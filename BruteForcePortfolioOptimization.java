import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BruteForcePortfolioOptimization{

private static class Asset {
        String id;
        double expectedReturn;
        double individualrisk;
        double units;

        public Asset(String id, double expectedReturn, double risk, double units) {
            this.id = id;
            this.expectedReturn = expectedReturn;
            this.individualrisk = risk;
            this.units = units;
        }
    }
    // Portfolio class representing each client
    private static class Portfolio {
        int[] allocation; // Percentage how much of each asset in total investment
        double expectedReturn;
        double risk;

    public Portfolio(int asset1, int asset2, int asset3) {
        allocation = new int[3];
        allocation[0] = asset1 ;
        allocation[1] = asset2 ;
        allocation[2] = asset3 ;
    }

    // Check if sum of allocation percents add up to 100 
    /*public boolean isValid(double totalInvestment) {
        double sum = 0.0;
        for (int i = 0; i < allocation.length; i++) {
            sum += allocation[i];
        }
        return Math.abs(sum - 1.0) < 0.000001;//As long as the absolute difference is less than 0.000001, the weight sum is considered valid. 
    }*/

    public void calculatePortfolioEfficiency(List<Asset> assets,int totalInvestment) {
        expectedReturn = 0.0;
        risk = 0.0;
        for (int i = 0; i < assets.size(); i++) {
            expectedReturn += allocation[i] * assets.get(i).expectedReturn; //return of each asset in portfolio allocation
            risk += allocation[i] * assets.get(i).individualrisk; //risk of each asset in portfolio allocation
        }
        expectedReturn = expectedReturn/totalInvestment;
        risk = risk/totalInvestment;
    }
}
 
     public static void main(String[] args) {
      String fileName = "investment.txt"; 
      //Initialize arrays for 3 assets
      List<Asset> assets = new ArrayList<>();
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
             //Store
             assets.add(new Asset(id, expectedReturn, riskLevel, quantity));
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
         Portfolio optimalPortfolio =bruteForceOptimizePortfolio(assets, totalInvestment, riskTolerance);
         
         //printing the results
         System.out.println("Optimal Allocation");
         for (int i =0; i< assets.size(); i++)   {
             System.out.printf("%s: %d units\n",
                    assets.get(i).id, optimalPortfolio.allocation[i]);
         }
         System.out.printf("Expected Portfolio Return: %.4f\n", optimalPortfolio.expectedReturn);
         System.out.printf("Portfolio Risk Level: %.4f\n", optimalPortfolio.risk);
     }
     
     private static Portfolio bruteForceOptimizePortfolio(List<Asset> assets, int totalInvestment, double riskTolerance) {
        Portfolio optimalPortfolio = null;
        double maxReturn = 0;
        Portfolio portfolio = new Portfolio(0, 0, 0);
        int k =0;
        
        for (int i =0; i<=assets.get(0).units; i++)  { //first loop for the first asset
          for (int j =0; j<=assets.get(1).units; j++)  { //second loop for the second asset
             if (i+j >totalInvestment)
                break;
                            
             k = totalInvestment - (i+j); //to keep the quantity for the third asset
             
             if (k >assets.get(2).units)
               break;
               
             portfolio = new Portfolio(i, j, k);
             portfolio.calculatePortfolioEfficiency(assets,totalInvestment); // Calculate portfolio return and risk
               
             
             if (portfolio.risk <= riskTolerance && portfolio.expectedReturn > maxReturn) {
                        maxReturn = portfolio.expectedReturn;
                        optimalPortfolio = portfolio;
             }
                 
          }    
       } 
      return optimalPortfolio;   
     } 
 }
