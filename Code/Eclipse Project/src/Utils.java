
public class Utils {
	public static String padLeft(String a, int size, char pc)
	{
		int length=a.length();
		if(length<size)
		{
			for(int i=0;i<size-length;i++)
				a=pc+a;
		}
		return a;
	}
}
