package agency.highlysuspect.toybox.gdiff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementation of the gdiff algorithm, as described by https://www.w3.org/TR/NOTE-gdiff-19970825.html
 */
public class GdiffPatcher {
	public byte[] apply(byte[] original, InputStream patch) throws IOException {
		//most patches add things, i'd guess
		return apply(original, patch, (int) (original.length * 1.2));
	}
	
	public byte[] apply(byte[] original, InputStream patch, int outputBufferSizeEstimate) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(outputBufferSizeEstimate);
		apply(original, patch, out);
		return out.toByteArray();
	}
	
	public void apply(byte[] originalBytes, InputStream patch, OutputStream out) throws IOException {
		int magic = readMagic(patch);
		if(magic != 0xD1FFD1FF) throw new IOException("Invalid magic: " + Integer.toHexString(magic) + ", expected 0xD1FFD1FF");
		
		int version = readUbyte(patch);
		if(version != 4) throw new IOException("Invalid version: " + version + ", expected version 4");
		
		done: while(true) {
			int instruction = readUbyte(patch);
			if(instruction == -1) throw new IOException("Unexpected end-of-patch");
			assert instruction <= 256;
			
			switch(instruction) {
				//Instruction 0: end.
				case 0: break done;
				
				//Instructions 247/248: read (ushort/uint), copy that many bytes from patch to output.
				case 247: copyFromPatch(patch, readUshort(patch), out); break; //<- forge patches use this
				case 248: copyFromPatch(patch, readInt(patch),    out); break;
				
				//Instructions 249..=255: copy a segment of the original file into the output.
				//first read "absolute byte offset in original file", then read "length to copy".
				//Data types vary per-instruction to accomodate different sizes of number.
				case 249: out.write(originalBytes, readUshort(patch),    readUbyte(patch));  break; //<- forge patches use this
				case 250: out.write(originalBytes, readUshort(patch),    readUshort(patch)); break;
				case 251: out.write(originalBytes, readUshort(patch),    readInt(patch));    break;
				case 252: out.write(originalBytes, readInt(patch),       readUbyte(patch));  break;
				case 253: out.write(originalBytes, readInt(patch),       readUshort(patch)); break;
				case 254: out.write(originalBytes, readInt(patch),       readInt(patch));    break;
				case 255: out.write(originalBytes, readTruncLong(patch), readInt(patch));    break;
				
				//Instructions 1..=246: copy [instruction] many bytes from patch to output.
				default:  copyFromPatch(patch, instruction,       out); break; //<- forge patches use this
			}
		}
		
	}
	
	protected void copyFromPatch(InputStream patch, int howMany, OutputStream out) throws IOException {
		byte[] shuttle = new byte[howMany];
		int remaining = howMany;
		while(remaining > 0) {
			int read = patch.read(shuttle, 0, remaining);
			out.write(shuttle, 0, read);
			remaining -= read;
		}
	}
	
	protected int readUbyte(InputStream in) throws IOException  {
		return in.read();
	}
	
	protected int readUshort(InputStream in) throws IOException  {
		return (int) readBigEndian(in, 2, "ushort");
	}
	
	protected int readMagic(InputStream in) throws IOException  {
		return (int) readBigEndian(in, 4, "magic number");
	}
	
	//"If a number larger than 1^31-1 bytes is needed for a command that takes only int arguments,
	//the command must be split into multiple commands."
	protected int readInt(InputStream in) throws IOException  {
		int result = (int) readBigEndian(in, 4, "int");
		if(result < 0) throw new IOException("int with the high bit set");
		return result;
	}
	
	//We assume the input file fits in a Java array (<2gb)
	protected int readTruncLong(InputStream in) throws IOException  {
		long result = readBigEndian(in, 8, "long");
		if(result < 0 || result > Integer.MAX_VALUE) throw new IOException("long that can't be truncated to an int");
		return (int) result;
	}
	
	protected long readBigEndian(InputStream in, int bytes, String type) throws IOException {
		long result = 0;
		for(int i = 0; i < bytes; i++) {
			result <<= 8;
			
			int read = in.read();
			if(read == -1) throw new IOException("Unexpected end of patch (byte " + i + " of " + type + ")");
			
			result |= read;
		}
		return result;
	}
	
	//I should really make this a util function
	//or just upgrade to java 17...
	protected byte[] readAllBytes(InputStream in) throws IOException {
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		byte[] shuttle = new byte[4096];
		int read;
		while((read = in.read(shuttle)) != -1) o.write(shuttle, 0, read);
		return o.toByteArray();
	}
}
