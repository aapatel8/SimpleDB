package simpledb.buffer;

import java.util.LinkedHashMap;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
   private Buffer[] bufferpool;
   private int numAvailable;
	   
   //New bufferPool for CSC540 Project2
   private BufferPoolMap bufferPoolMap;
   
   //2 Methods Required by TA's
   /**
   * Determines whether the map has a mapping from
   * the block to some buffer.
   * @param blk the block to use as a key
   * @return true if there is a mapping; false otherwise */
   public boolean containsMapping(Block blk) { 
	   return bufferPoolMap.containsKey(blk);
   }
   /**
   * Returns the buffer that the map maps the specified block to.
   * @param blk the block to use as a key
   * @return the buffer mapped to if there is a mapping; null otherwise */
   public Buffer getMapping(Block blk) {
	   return bufferPoolMap.get(blk);
   }
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   BasicBufferMgr(int numbuffs) {
	   //Initial thoughts, not sure if right way tho...
	   bufferPoolMap = new BufferPoolMap(numbuffs);
	   numAvailable = numbuffs;
	   
	   
//      bufferpool = new Buffer[numbuffs];
//      numAvailable = numbuffs;
//      for (int i=0; i<numbuffs; i++)
//         bufferpool[i] = new Buffer();
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
	   //New Method
	   bufferPoolMap.flush(txnum);
	   
	   
	   
//      for (Buffer buff : bufferpool)
//         if (buff.isModifiedBy(txnum))
//         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) {
	   //If none available, then return null, catch outside of this and do 
	   if (numAvailable == 0){
		   return null;
	   }
	   Buffer buff = findExistingBuffer(blk);
	   if (buff == null) {
		   //block not in map so first just try to add new buffer, ONLY if queue is not full
		   buff = bufferPoolMap.addNew(blk);
		   if (buff == null){
			   //block was not in the map AND map is at capacity/full, so use fifo to remove oldest unpinned buffer
			   buff = bufferPoolMap.chooseBufferFIFO();
			   if (buff == null) {
				   //no unpinned buffers!
				   return null;
			   }
			   //FOR TESTING BUFFERREPLACEMENTFIFOTEST.JAVA ONLY
			   //System.out.println("FIFO Selected: blk # " + buff.block().number());
			   buff.assignToBlock(blk);
			   //b/c it was replaced we add it to the end of the queue
			   bufferPoolMap.addToEndOfQueue(buff);
		   }
	   }
	   if (!buff.isPinned())
			numAvailable--;
	   
	   buff.pin();
	   return buff;
	   
	   
	   //Buffer buff = findExistingBuffer(blk);
//      if (buff == null) {
//         buff = chooseUnpinnedBuffer();
//         if (buff == null)
//            return null;
//         buff.assignToBlock(blk);
//      }
//      if (!buff.isPinned())
//         numAvailable--;
//      buff.pin();
//      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
	   //If none available, then return null, catch outside of this and do 
	   if (numAvailable == 0){
		   return null;
	   }
	   //First just try to add new buffer, if queue is not full
	   Buffer buff = bufferPoolMap.addNew(filename, fmtr);
	   if (buff == null) {
		   //queue was full, so use fifo to remove oldest unpinned buffer
		   buff = bufferPoolMap.chooseBufferFIFO();
		   buff.assignToNew(filename, fmtr);
		   //b/c it was replaced we add it to the end of the queue
		   bufferPoolMap.addToEndOfQueue(buff);
	   }
	   numAvailable--;
	   buff.pin();
	   return buff; 
	   
	   
//      Buffer buff = chooseUnpinnedBuffer();
//      if (buff == null)
//         return null;
//      buff.assignToNew(filename, fmtr);
//      numAvailable--;
//      buff.pin();
//      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
         numAvailable++;
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private Buffer findExistingBuffer(Block blk) {
	   //New Method
	   return bufferPoolMap.findExistingBuffer(blk);
	   
//      for (Buffer buff : bufferpool) {
//         Block b = buff.block();
//         if (b != null && b.equals(blk))
//            return buff;
//      }
//      return null;
   }
   
   private Buffer chooseUnpinnedBuffer() {
      for (Buffer buff : bufferpool)
         if (!buff.isPinned())
         return buff;
      return null;
   }
}
