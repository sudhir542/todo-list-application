package nice.services;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilServiceForTests {

	 public byte[] toJson(Object r) throws Exception {
	        ObjectMapper map = new ObjectMapper();
	        return map.writeValueAsString(r).getBytes();
	    }
	    
	 public long getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }
}
