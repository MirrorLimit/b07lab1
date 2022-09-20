public class Polynomial
{
	double [] coefficients;
	public Polynomial()
	{
		coefficients = new double[1];
		coefficients[0] = 0.0;
	}

	public Polynomial(double [] input)
	{
		coefficients = new double[input.length];
		for(int i = 0; i < input.length; i++)
		{
			coefficients[i] = input[i];
		}
	}

	public Polynomial add(Polynomial input)
	{
		Polynomial output;
		int maxLength = Math.max(input.coefficients.length, coefficients.length);
		if (maxLength == input.coefficients.length)
		{
			output = new Polynomial(input.coefficients);
		}
		else
		{
			output = new Polynomial(coefficients);
		}
		

		if (maxLength == input.coefficients.length)
		{
			for(int i = 0; i < coefficients.length; i++)
			{
				output.coefficients[i] = output.coefficients[i] + coefficients[i];
			}
		}
		
		else
		{
			for(int i = 0; i < input.coefficients.length; i++)
			{
				output.coefficients[i] = output.coefficients[i] + input.coefficients[i];
			}
		}
		return output;
	}

	public double evaluate(double x)
	{
		double output = 0.0;
		for(int i = 0; i < coefficients.length; i++)
		{
			output = coefficients[i] * Math.pow(x, i) + output;
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
}