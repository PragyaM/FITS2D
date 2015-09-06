package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.ArrayList;

import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.fits.TruncatedFileException;
import nom.tam.util.BufferedDataInputStream;
import nom.tam.util.Cursor;
import uk.ac.starlink.ast.FitsChan;

public class ChanneliseFitsHeader {

	public ChanneliseFitsHeader(){
	}

	public static FitsChan chanFromHeaderObj(Header header){
		Cursor<String, HeaderCard> iter = header.iterator();

		FitsChan chan = new FitsChan();

		while (iter.hasNext()){
			HeaderCard card = (HeaderCard) iter.next();
			String cardVal = String.valueOf(card);

			try {
				chan.putFits(cardVal, true);
			} catch(Exception e){
				e.printStackTrace();
				System.out.println("Skipping troublesome header card: " + cardVal);
			}
		}

		return chan;
	}

	public static FitsChan chanFromHeaderString(String headerString){

		Header header = new Header();

		/* chunk header string into an array of card strings */
		ArrayList<String> cardStrings = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new StringReader(headerString));
		String line;
		try {
			line = reader.readLine();
			while (line != null){
				cardStrings.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* need to convert each card string to a HeaderCard one by one 
		 * so that any troublesome cards are skipped*/
		for (String cardString : cardStrings){
			InputStream is = new StringBufferInputStream(cardString);
			BufferedDataInputStream bdis = new BufferedDataInputStream(is);
			try {
				HeaderCard card = new HeaderCard(bdis);
				header.addLine(card);
				is.close();
				bdis.close();
			} catch (TruncatedFileException | IOException e) {
				System.out.println("Skipping troublesome card: " + cardString);
			}
		}
		return chanFromHeaderObj(header);
	}

}
