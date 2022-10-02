import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Driver { 
 public static void main(String [] args) throws FileNotFoundException, IOException { 
  Polynomial p = new Polynomial(); 
  System.out.println(p.evaluate(3)); //should get 0
  double [] c1 = {6,0,0,5}; 
  int [] e1 = {1, 2, 3, 4};
  Polynomial p1 = new Polynomial(c1, e1); 
  System.out.println("printing p1 below--------------");
  //System.out.printf(" p1 coefficients length: %d", p1.coefficientsNZ.length);
  for (int i = 0; i < p1.coefficientsNZ.length; i++)
  {
	  System.out.print(p1.coefficientsNZ[i]);
	  System.out.print(", ");
	  System.out.print(p1.expo[i]);
	  System.out.print("; ");
  }
  System.out.println();
  double [] c2 = {1,-2,0,0,-9}; 
  int [] e2 = {0, 2, 4, 3, 5};
  Polynomial p2 = new Polynomial(c2, e2); 
  System.out.println("printing p2 below--------------");
  for (int i = 0; i < p2.coefficientsNZ.length; i++)
  {
	  System.out.print(p2.coefficientsNZ[i]);
	  System.out.print(", ");
	  System.out.print(p2.expo[i]);
	  System.out.print("; ");
  }
  System.out.println();
  Polynomial s = p1.add(p2); //should get -9x^5+5x^4-2x^2+6x+1
  for (int i = 0; i < s.coefficientsNZ.length; i++)
  {
	  System.out.print(s.coefficientsNZ[i]);
	  System.out.print(", ");
	  System.out.print(s.expo[i]);
	  System.out.print("; ");
  }
  System.out.println();
  System.out.println("s(0.1) = " + s.evaluate(0.1)); 
  if(s.hasRoot(1)) 
   System.out.println("1 is a root of s"); 
  else 
   System.out.println("1 is not a root of s"); 
  
  //testing multiply
  //p1 * p2 should be -45x^9 - 64x^6 + 5x^4 - 12x^3 + 6x
  Polynomial product = p1.multiply(p2);
  System.out.println("printing product now:-------------------------");
  for (int i = 0; i < product.coefficientsNZ.length; i++)
  {
	  System.out.print(product.coefficientsNZ[i]);
	  System.out.print(", ");
	  System.out.print(product.expo[i]);
	  System.out.print("; ");
  }
  System.out.println();
  
  //testing file constructor with a file with a valid expression, not constant; test A; "3x2-8+5x9"
  //test A passed
  //testing file constructor with an empty file; test B; ""
  //test B passed
  //testing file constructor with a constant term; test C; "+7"
  //test C passed
  File fileTest = new File("C:/Users/Killt/b07lab1/file input testing.txt");
  Polynomial fileTestPoly = new Polynomial(fileTest);
  System.out.println("printing test C now:-------------------------");
  for (int i = 0; i < fileTestPoly.coefficientsNZ.length; i++)
  {
	  System.out.print(fileTestPoly.coefficientsNZ[i]);
	  System.out.print(", ");
	  //System.out.print(fileTestPoly.expo[i]);
	  //System.out.print("; ");
  }
  System.out.println();
	 
  //testing saveToFile with a normal expression; test D; 3x2-8+5x9
  //test D passed, prints in decimal forms
  //testing saveToFile with the 0 polynomial; test E; null for both arrays
  //test E passed
  //testing saveToFile with a constant term; test F; +7
  double [] fileTestC = {7.0};
  int [] fileTestE = {0};
  fileTestPoly = new Polynomial(fileTestC, fileTestE);
  fileTestPoly.saveToFile("C:/Users/Killt/b07lab1/file output testing.txt");
 } 
} 