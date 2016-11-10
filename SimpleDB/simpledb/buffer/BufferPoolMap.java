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
	
	/**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
	public void flush(int txnum){
		for (Map.Entry<Block, Buffer> entry : bufferPoolMap.entrySet()) {
		    //Block blk = entry.getKey();
		    Buffer buff = entry.getValue();
		    if (buff.isModifiedBy(txnum))
		         buff.flush();
		}
	}
	
	
	//2 TA Methods
	public boolean containsKey(Block blk){
		return bufferPoolMap.containsKey(blk);
	}
	
	public Buffer get(Block blk) {
	   return bufferPoolMap.get(blk);
    }

	public Buffer findExistingBuffer(Block blk) {
		return get(blk);
	}
	
	
}
