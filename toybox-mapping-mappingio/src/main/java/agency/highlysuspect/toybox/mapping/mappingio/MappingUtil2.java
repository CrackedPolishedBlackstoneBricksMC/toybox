package agency.highlysuspect.toybox.mapping.mappingio;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MappingUtil2 {
	/**
	 * Copy-and-paste of MappingUtil.mapDesc but taking a Function instead of a map
	 *
	 * TODO push something like this up into toybox-mapping
	 */
	public static String mapDesc(String desc, int start, int end, Function<String, @Nullable String> clsMap) {
		StringBuilder ret = null;
		int searchStart = start;
		int clsStart;
		
		while ((clsStart = desc.indexOf('L', searchStart)) >= 0) {
			int clsEnd = desc.indexOf(';', clsStart + 1);
			if (clsEnd < 0) throw new IllegalArgumentException();
			
			String cls = desc.substring(clsStart + 1, clsEnd);
			String mappedCls = clsMap.apply(cls); //CHANGE
			
			if (mappedCls != null) {
				if (ret == null) ret = new StringBuilder(end - start);
				
				ret.append(desc, start, clsStart + 1);
				ret.append(mappedCls);
				start = clsEnd;
			}
			
			searchStart = clsEnd + 1;
		}
		
		if (ret == null) return desc.substring(start, end);
		
		ret.append(desc, start, end);
		
		return ret.toString();
	}
}
