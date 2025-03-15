import agency.highlysuspect.toybox.string.DummyStringInterner;
import agency.highlysuspect.toybox.string.JdkStringInterner;
import agency.highlysuspect.toybox.string.MapStringInterner;
import agency.highlysuspect.toybox.string.StringInterner;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ToyboxStringTests {
	@SuppressWarnings("StringOperationCanBeSimplified") //the whole point
	protected String not(String in) {
		String different = new String(in);
		assertNotSame(different, in);
		return different;
	}
	
	protected void basic(StringInterner mem) {
		String a = mem.intern("hello");
		String b = mem.intern(not("hello"));
		assertSame(a, b);
	}
	
	protected String[] firstPieces = {"foo", "bar", "baz", "quux", "fizz", "buzz"};
	protected String[] lastPieces =  {"yes", "no", "maybe-so", "try-again-later", "ask-again"};
	
	@SuppressWarnings("StringEquality")
	protected void prop(StringInterner mem) {
		int len = 500;
		
		//fill array with random strings
		Random r = new Random(42);
		String[] orig = new String[len];
		for(int i = 0; i < len; i++) {
			orig[i] = not(firstPieces[r.nextInt(firstPieces.length)] + "-" + lastPieces[r.nextInt(lastPieces.length)]);
		}
		
		//assert all the strings are unique objects
		for(int i = 0; i < len; i++) {
			for(int j = i + 1; j < len; j++) {
				assertNotSame(orig[i], orig[j], "identical strings pre-deduplication (test bug)");
			}
		}
		
		//deduplicate them
		String[] deduped = new String[len];
		System.arraycopy(orig, 0, deduped, 0, len);
		mem.internArray(deduped);
		
		//didn't modify values
		assertArrayEquals(orig, deduped);
		
		//equals iff identical
		boolean foundTrue = false, foundFalse = false;
		for(int i = 0; i < len; i++)  {
			for(int j = i + 1; j < len; j++) {
				boolean equals = deduped[i].equals(deduped[j]);
				boolean ident = deduped[i] == deduped[j];
				assertEquals(equals, ident, "equals didn't match identical");
				
				//check that we're exercising both arms
				if(equals) foundTrue = true;
				else foundFalse = true;
			}
		}
		assertTrue(foundTrue, "didn't find equal strings in array");
		assertTrue(foundFalse, "didn't find non-equal strings in array");
	}
	
	@Test
	public void jdkBasic() {
		basic(new JdkStringInterner());
	}
	
	@Test
	public void mapBasic() {
		basic(new MapStringInterner());
	}
	
	@Test
	public void jdkProp() {
		prop(new JdkStringInterner());
	}
	
	@Test
	public void mapProp() {
		prop(new MapStringInterner());
	}
	
	@Test
	public void dummyFailsBasic() {
		assertThrows(Throwable.class, () -> basic(DummyStringInterner.INSTANCE));
	}
	
	@Test
	public void dummyFailsProp() {
		assertThrows(Throwable.class, () -> prop(DummyStringInterner.INSTANCE));
	}
}
