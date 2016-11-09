package simpledb.buffer;

import java.util.Map;
import java.util.LinkedHashMap;

import simpledb.file.*;

/**
 * BufferPoolMap class implemented as a Queue!!!
 * @author Vaibhav Chawla (vchawla3)
 */
public class BufferPoolMap {
	private LinkedHashMap<Block,Buffer> bufferPoolMap;
	
	public BufferPoolMap(int numbuffs){
		bufferPoolMap = new LinkedHashMap<Block,Buffer>(numbuffs);
	}
	
	
	
	
	//2 TA Methods
	public boolean containsKey(Block blk){
		return bufferPoolMap.containsKey(blk);
	}
	
	public Buffer get(Block blk) {
	   return bufferPoolMap.get(blk);
    }
	
	
}
