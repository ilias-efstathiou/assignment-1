import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;


public class StockSpan {
	static ArrayList<String> dates = new ArrayList<String>(); // h domh dedomenwn pou apothikeuei tis meres
	static ArrayList<Float> values = new ArrayList<Float>(); // h domh dedomenwn pou apothikeuei tis times
	static int[] spans; // o pinakas me tous dixtes pou upologisame
	
	public static void main(String[] args) {
		
		// an o xrhsths den mas exei dwsei orismata typonoume ena minuma lathous kai ton swsto tropo na treksei to programma 
		if (args.length == 0){
			System.out.println("Wrong usage!");
			System.out.println("Use \"java StockSpan -n filename\" for naive implementation");
			System.out.println("Use \"java StockSpan -s filename\" for stack implementation");
			System.out.println("Use \"java StockSpan -b filename\" for both implementations");
			return;
		}
		
		
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(args[1])); //dhmiourgoume to antikeimeno pou tha diabasei to arxeio
			
			//diavazoume thn prwth gramh
			String line = buffer.readLine();
			while (line != null) {
				// xwrizoume thn gramh se duo string 
				String[] str = line.split(",");
				// an ta string den einai DATE kai VALUE (dhladh h prwth gramh tou arxeio) ta apothikeuoume stis domes dedomenwn 
				if (!str[0].equals("DATE") && !str[1].equals("VALUE")) {
					dates.add(str[0]);
					values.add(new Float(Float.parseFloat(str[1])));
				}
				// diavazoume thn epomenh gramh
				line = buffer.readLine();
			}
			
			buffer.close();
		
		// se periptwsh eksereshs typwnoume to katalhlo mhnuma
		} catch (FileNotFoundException e) {
			System.out.println("File " + args[1] + " not found!");
			e.printStackTrace();
		} catch(IOException e) {
			System.out.println("Error trying to read from file.");
			e.printStackTrace();
		}
		
		
		// analoga me ta orismata pou mas edwse o xrhsths kaloume thn katalhlh ylopoihsh
		if (args[0].equals("-n"))
			naiveImpl();
		else if (args[0].equals("-s"))
			stackImpl();
		else if (args[0].equals("-b")) 
			bothImpl();
		
		
		// typwnoume to apotelesma an xreiazete
		if (!args[0].equals("-b")) {
			for (int i = 0; i < dates.size(); i++) {
				System.out.println(dates.get(i) + "," + spans[i]);
			}
		}
	}
	
	public static void naiveImpl() {
		// dhmiourgoume ton pinaka mas
		spans = new int[dates.size()];
		
		//to prwto stoixeio einai panta 1
		spans[0] = 1;
		for (int i = 1; i < dates.size(); i++) {
			spans[i] = 1;
			
			// gia kathe stoixeio koitame ta prohgoumena kai to auksanoume kata ena gia kathe mikrotero stoixeio pou briskoume
			for (int j = i - 1; (j >= 0) && (values.get(i) >= values.get(j)); j--) {
				spans[i]++;
			}
		}
	}
	
	public static void stackImpl() {
		// dhmiourgoume thn stoiva
		Stack<Integer> ms = new Stack<>();
		
		
		spans = new int[dates.size()];
		if (spans.length == 0)
			return;
		
		//to prwto stoixeio einai panta 1
		spans[0] = 1;
		
		// bazoume ton dixtu tou prwtou stoixeiou sthn stoiva
		ms.push(0);
		
		// gia kathe stoixeio bgazoume ola ta mikrotera stoixeia apo thn stoiva, allazoume to span analoga me to posa stoixeia bgalame kai topothetoume to stoixeio sthn stoiva 
		for (int i = 1; i < dates.size(); i++) {
			
			while (values.get(i) > values.get(ms.peek())) {
				ms.pop();
				if (ms.isEmpty())
					break;
			}
			
			if (ms.isEmpty()) {
				spans[i] = i + 1;
			} else {
				spans[i] = i - ms.peek();
			}
			
			ms.push(i);
		}
	}
	
	public static void bothImpl() {
		// upologizoume ton xrono pou xreiazetai to programma na treksei 100 fores thn kathe ylopoihsh 
		long naivetime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++)
			naiveImpl();
		naivetime = System.currentTimeMillis() - naivetime;
		
		long stacktime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++)
			stackImpl();
		stacktime = System.currentTimeMillis() - stacktime;
		
		// tupwnoume ta apotelesmata
		System.out.println("Naive implementation took: " + naivetime + " millis");
		System.out.println("Stack implementation took: " + stacktime + " millis");
	}
}
