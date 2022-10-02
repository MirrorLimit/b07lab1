import java.util.Arrays;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Polynomial
{
	//sets the 0 polynomial to null
	double [] coefficientsNZ;//stores nonzero coefficients
	int [] expo;//stores the exponents on the nonzero coefficients
	
	public Polynomial()
	{
		coefficientsNZ = null;
		expo = null;
	}

	public Polynomial(double [] inputCof, int [] inputExpo)
	{
		int count = 0;
		coefficientsNZ = new double[inputCof.length];
		for(int i = 0; i < inputCof.length; i++)
		{
			if (inputCof[i] != 0)// this part actually puts gaps in the array, needs to take care
			{
				coefficientsNZ[count] = inputCof[i];
				count = count + 1;
			}
		}
		coefficientsNZ = Arrays.copyOf(coefficientsNZ, count);//this removes extra 0s
		count = 0;
		
		if (Arrays.binarySearch(coefficientsNZ, 0.0) < 0)//handles 0 polynomial
		{
			
			expo = new int[inputExpo.length];
			for(int i = 0; i < inputExpo.length; i++)
			{
				if (inputCof[i] != 0)//doesn't put in the exponent in case coefficient is 0 to keep length the same
				{
					expo[count] = inputExpo[i];
					count = count + 1;
				}
			}
			expo = Arrays.copyOf(expo, count);
		}
		else
		{
			coefficientsNZ = null;
			expo = null;
		}
		
	}
	
	public Polynomial(File inputF) throws FileNotFoundException, IOException
	{
		//open the file, then parse, add things to the arrays
		BufferedReader opened = new BufferedReader(new FileReader(inputF));
		String line = opened.readLine();
		
		/*-3x2+5+7x8
		split with x
		"-3", "2+5+7", "8"

		-3x2+5+7x8
		split with +
		"-3x2", "5", "7x8"
		split with - on each substring
		"3x2", "5", "7x8"
		split with x on each substring
		"3", "2", "5", "7", "8" THIS PART GETS ALL THE NUMBERS BUT UNSIGNED AND DON'T KNOW IF IT'S COEFFICIENTOF EXPONENENT
		
		CAN TRY TO USE indexOf ON ALL THESE SUBSTRINGS STARTING AT THE BEGINNING. AFTER FINDING THE INDEX, LOOK AT THE INDEX RIGHT BEFORE
		FOR THE SIGN. IF INDEX BEFORE IS OUT OF BOUNDS THEN IT'S POSITIVE, ELSE THEN CHECK TO SEE IF IT'S A COEFFICIENT (+/-) OR IF IT'S AN
		EXPONENET (X INFRONT OF IT). ADD TO THE CORRESPONDING ARRAYS*/
		
		if (line != null)
		{
			String [] subs = line.split("\\+"); //this split with a constant term "+7" actually makes {"", "+7}
			String [] newSubs = new String[0];
			String [] newSubs2 = new String[0];
			String [] newSubs3 = new String[0];
			int index = 0;
			int fromI = 0;
			int outCI = 0;
			int outEI = 0;
			
			double [] outCoef = new double [0];
			int [] outExpo = new int [0];
			for (String i:subs)
			{
				index = newSubs.length;
				newSubs = Arrays.copyOf(newSubs, (i.split("-").length + newSubs.length));
				newSubs2 = i.split("-");
				for (int j = index; j < newSubs.length; j++)//loops to put in the new strings of spliting by "-"
				{
					newSubs[j] = newSubs2[j - index];
				}
			}//now newSubs should contain numbers and x but no signs. Need to split with x
			
			
			for (String i:newSubs)
			{
				index = newSubs3.length;
				newSubs3 = Arrays.copyOf(newSubs3, (i.split("x").length + newSubs3.length));
				newSubs2 = i.split("x");
				for (int j = index; j < newSubs3.length; j++)
				{
					newSubs3[j] = newSubs2[j - index];
				}
			}//now newSubs should have all numbers. Need to figure out the signs and if they're coefficients or exponenents
			
			for (String i:newSubs3)
			{
				//newSubs3 should be the one used as it has all the coefficients
				if (i != "")
				{
					index = line.indexOf(i, fromI);
					if ((index - 1) < 0)//i is the first thing in the string so it must be a coefficient, positive
					{
						outCoef = Arrays.copyOf(outCoef, outCoef.length + 1);
						outCoef[outCI] = Double.parseDouble(i);
						outCI = outCI + 1;
						if ((index + 1) < line.length() && line.charAt(index + 1) != 'x')//constant term
						{
							outExpo = Arrays.copyOf(outExpo, outExpo.length + 1);
							outExpo[outEI] = 0;
							outEI = outEI + 1;
						}
					}
					else if (line.charAt(index - 1) == 'x')//exponent
					{
						outExpo = Arrays.copyOf(outExpo, outExpo.length + 1);
						outExpo[outEI] = Integer.parseInt(i);
						outEI = outEI + 1;
					}
					else if (line.charAt(index - 1) == '+')//+ coefficient
					{
						outCoef = Arrays.copyOf(outCoef, outCoef.length + 1);
						outCoef[outCI] = Double.parseDouble(i);
						outCI = outCI + 1;
						if ((index + 1) < line.length() && line.charAt(index + 1) != 'x')//constant term
						{
							outExpo = Arrays.copyOf(outExpo, outExpo.length + 1);
							outExpo[outEI] = 0;
							outEI = outEI + 1;
						}
					}
					else if (line.charAt(index - 1) == '-')//- coefficient
					{
						outCoef = Arrays.copyOf(outCoef, outCoef.length + 1);
						outCoef[outCI] = Double.parseDouble("-" + i);
						outCI = outCI + 1;
						if ((index + 1) < line.length() && line.charAt(index + 1) != 'x')//constant term
						{
							outExpo = Arrays.copyOf(outExpo, outExpo.length + 1);
							outExpo[outEI] = 0;
							outEI = outEI + 1;
						}
					}
				}
			}
			this.coefficientsNZ = outCoef;
			this.expo = outExpo;
			opened.close();
		}
	}
	
	public void saveToFile (String path) throws FileNotFoundException, IOException// assumes file is empty and writes to 1 line
	{
		//need to go through expo and coef at the same time, put in coefficient first and then
		//check the exponent, if exponent = 0 than it is a constant, if exponent > 0 then
		//need to add x in with exponent right after
		
		File outfile = new File (path);
		PrintStream output = null;
		if (outfile.exists())
		{
			if (outfile.canRead() && outfile.isFile())
			{
				output = new PrintStream(outfile);
			}
			else
			{
				outfile.delete();
				outfile.createNewFile();
				output = new PrintStream(outfile);
			}
		}
		else
		{
			outfile.createNewFile();
			output = new PrintStream(outfile);
		}
		if (this.coefficientsNZ == null && this.expo == null)
		{
			output.print(0);
			output.close();
			return;
		}

		for (int i = 0; i < this.coefficientsNZ.length; i++)
		{
			//write the coefficient into the file, check if the exponent is 0 or not and put in
			//x accordingly
			if (this.coefficientsNZ[i] > 0)//need to add a detection to see if I need to put in + for the beginning or not
			{
				if (i != 0)
				{
					output.printf("+");
				}
				output.printf("%f", this.coefficientsNZ[i]);
			}
			else//this assumes that a negative coefficient is printed with the signs
			{
				output.printf("%f", this.coefficientsNZ[i]);
			}
			//check for exponents adds them in
			if (this.expo[i] != 0)
			{
				output.printf("%c", 'x');
				output.printf("%d", this.expo[i]);
			}
		}
		output.close();
		
	}

	public Polynomial add(Polynomial input)
	{
		Polynomial output = new Polynomial(this.coefficientsNZ, this.expo);
		int indexExpo = -1;
		int indexThis = -1;
		int indexIn = -1;
		
		if (output.coefficientsNZ == null)
		{
			return input;
		}
		else if (input.coefficientsNZ == null)
		{
			return output;
		}
		else
		{
			Arrays.sort(expo);
			
			for (int x:input.expo)
			{
				//look for x in output, if x isn't in output, add it
				indexExpo = Arrays.binarySearch(output.expo, x);
				if (indexExpo < 0 || indexExpo >= output.expo.length)//not found
				{
					output.expo = Arrays.copyOf(output.expo, output.expo.length + 1);//copies old expo over with 1 extra place to add in x
					output.expo[output.expo.length - 1] = x;
				}
			}
			Arrays.sort(output.expo);
			//exponents are all in
			output.coefficientsNZ = new double[output.expo.length];
			
			for (int i = 0; i < output.expo.length; i++)//loop through output.expo and add the coefficients for the exponent
			{
				indexThis = Arrays.binarySearch(expo, output.expo[i]);
				indexIn = Arrays.binarySearch(input.expo, output.expo[i]);
				if (indexThis >=0 && indexThis < expo.length)
				{
					output.coefficientsNZ[i] = this.coefficientsNZ[indexThis];
				}
				if (indexIn >= 0 && indexIn < input.expo.length)
				{
					output.coefficientsNZ[i] = output.coefficientsNZ[i] + input.coefficientsNZ[indexIn];
				}
			}
		}
		
		return output;
	}

	public double evaluate(double x)
	{
		double output = 0.0;
		if (coefficientsNZ == null)
		{
			return 0.0;
		}
		else
		{
			for(int i = 0; i < coefficientsNZ.length; i++)
			{
				output = coefficientsNZ[i] * Math.pow(x, expo[i]) + output;
			}
		}
		return output;
	}

	public boolean hasRoot(double x)
	{
		if (evaluate(x) == 0.0)
		{
			return true;
		}
		return false;
	}
	
	public Polynomial multiply(Polynomial input)
	{
		//make a new expo array that stores all the resulting expo
		//after multiplying, check if the exponent is already in, if not, then add it into the
		//array. Sort the new expo. Make a new coef array of size new-expo.length. now multiply
		//again but for the coefficients, make sure the resultant is operated with the new-coef
		//entry
		
		
		//outExpo shouldn't be multiply as they're exponents, should be add
		
		if (this.coefficientsNZ == null || input.coefficientsNZ == null)
		{
			Polynomial out = new Polynomial();
			return out;
		}
		
		boolean first = true;
		
		int [] outExpo = null;
		int index = -1;
		for (int i = 0; i < this.expo.length; i++)
		{
			for (int j = 0; j < input.expo.length; j++)
			{
				if (outExpo == null)
				{
					outExpo = new int[1];
					outExpo[0] = this.expo[i] + input.expo[j];
				}
				else//this makes it so that if it is null it keeps renewing it
				{	
					index = Arrays.binarySearch(outExpo, this.expo[i] + input.expo[j]);
					first = false;
				}
				
				if (outExpo != null && first == false &&
						(index < 0 || index >= outExpo.length))
				{
						outExpo = Arrays.copyOf(outExpo, outExpo.length + 1);
						outExpo[outExpo.length - 1] = this.expo[i] + input.expo[j];
						Arrays.sort(outExpo);
				}
			}
		}//this nested loop should take care of putting together an array of of multiplied exponents

		double [] outCoef = new double[outExpo.length];
		
		for (int i = 0; i < this.expo.length; i++)
		{
			for (int j = 0; j < input.expo.length; j++)
			{
				index = Arrays.binarySearch(outExpo, this.expo[i] + input.expo[j]);
				outCoef[index] = outCoef[index] + this.coefficientsNZ[i] * input.coefficientsNZ[j];
			}
		}
		
		Polynomial output = new Polynomial(outCoef, outExpo);
		return output;
	}
	
}