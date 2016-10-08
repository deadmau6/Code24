package MAIN;

public class runner {

	public static void main(String[] args) throws Exception
	{
		System.out.println("start");
		
		Matroschka mat = new Matroschka();
		
		String data = "penis lord";
		String key = "ketamine";
		
		System.out.println(data + " " + key);
		
		byte[] penis = mat.encrypt(data, key);
		for(byte b: penis)
		{
			System.out.println("done " + b);
		}
		
		
		String massage = mat.decrypt(penis, key);
		
		System.out.println("unencrypted:" + massage);
	}
}
