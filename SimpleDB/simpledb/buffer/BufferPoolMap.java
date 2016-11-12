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
	private int capacity;
	
	public BufferPoolMap(int numbuffs){
		bufferPoolMap = new LinkedHashMap<Block,Buffer>(numbuffs);
		this.capacity = numbuffs;
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
	
	public Buffer chooseBufferFIFO(){
		return removeOldestUnpinned();
	}
	
	/**
	 * tries to create and add a new buffer to the queue, if queue not at capacity
	 * if at capacity, return null, FIFO required 
	 * @param filename
	 * @param fmtr
	 * @return new buffer or null
	 */
	public synchronized Buffer addNew(String filename, PageFormatter fmtr){
		if (bufferPoolMap.size() == this.capacity){
			return null;
		} else {
			Buffer buff = new Buffer();
			buff.assignToNew(filename, fmtr);
			bufferPoolMap.put(buff.block(), buff);
			return buff;
		}
	}
	
	/**
	 * adds buffer to the end of the queue
	 * @param buff
	 */
	public synchronized void addToEndOfQueue(Buffer buff){
		bufferPoolMap.put(buff.block(), buff);
	}
	
	/**
	 * removes and returns the oldest buffer that is unpinned in the queue/map
	 * @return the Buffer object that is at the head of the queue
	 */
	public synchronized Buffer removeOldestUnpinned(){
		//removes and returns first buffer in the queue that is NOT pinned AKA not in use
		boolean found = false;
		Buffer buff = null;
		for( Block blk : bufferPoolMap.keySet() ){
			if (!found){
				if (!bufferPoolMap.get(blk).isPinned()){
					buff = bufferPoolMap.remove(blk);
					found = true;
				}
			}
		}
		return buff;
	}
	
	/**
	 * returns but does NOT remove the head of the queue
	 * if empty queue returns null
	 * @return
	 */
	public Buffer peek(){
		//only return the first buffer or null if nothing in the queue/map
		boolean found = false;
		Buffer buff = null;
		for( Block blk : bufferPoolMap.keySet() ){
			if (!found){
				buff = bufferPoolMap.get(blk);
				found = true;
			}
		}
		return buff;
	}
}
