package simpledb.buffer;

import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class BufferReplacementFIFOTest {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args){
		SimpleDB.init("simpleDB");
		
		Block blk1 = new Block("filename", 1);
		Block blk2 = new Block("filename2", 2);
		Block blk3 = new Block("filename3", 3);
		Block blk4 = new Block("filename4", 4);
		Block blk5 = new Block("filename5", 5);
		Block blk6 = new Block("filename6", 6);
		Block blk7 = new Block("filename7", 7);
		Block blk8 = new Block("filename8", 8);
		Block blk9 = new Block("filename9", 9);
		
		BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
		
		System.out.println("Should be 8! Actually is ..." + basicBufferMgr.available());
		basicBufferMgr.pin(blk1);
		System.out.println(basicBufferMgr.available());
		basicBufferMgr.pin(blk2);
		System.out.println(basicBufferMgr.available());
		basicBufferMgr.pin(blk3);
		System.out.println(basicBufferMgr.available());
		basicBufferMgr.pin(blk4);
		System.out.println(basicBufferMgr.available());
		basicBufferMgr.pin(blk5);
		System.out.println(basicBufferMgr.available());
		basicBufferMgr.pin(blk6);
		System.out.println(basicBufferMgr.available());
		basicBufferMgr.pin(blk7);
		System.out.println(basicBufferMgr.available());
		basicBufferMgr.pin(blk8);
		System.out.println(basicBufferMgr.available());
		
		try {
			basicBufferMgr.pin(blk9);
	     	System.out.println("failed and got here");
	    } catch (BufferAbortException e) {
	    	System.out.println("success none left so got here");
	    }
		
		basicBufferMgr.unpin(basicBufferMgr.getMapping(blk3));
		basicBufferMgr.unpin(basicBufferMgr.getMapping(blk2));
		
		//GO TO LINE 102 ON BASICBUFFERMANAGER AND UNCOMMENT TO PRINT TO CONSOLE WHAT BUFF IS BEING CHOSEN BY FIFO
		//this is where 2 should be replaced w/ fifo - 2 should print!
		Buffer buff = basicBufferMgr.pin(blk9); 
		
		Block blk10 = new Block("filename10", 10);
		
		basicBufferMgr.unpin(basicBufferMgr.getMapping(blk1));
		buff = basicBufferMgr.pin(blk10);
		//1 should print. b/c it was oldest unpinned!
		
		Block blk11 = new Block("filename11", 11);
		buff = basicBufferMgr.pin(blk11);
		//3 should print. b/c it was oldest unpinned!
		
		
		try {
			//Should be 0 so next pin will fail
			System.out.println(basicBufferMgr.available());

			Block blk12 = new Block("filename12", 12);
			basicBufferMgr.pin(blk12);
	     	System.out.println("should not get here");
	    } catch (BufferAbortException e) {
	    	System.out.println("if gets here, good b/c no availalbe unpinned buffers");
	    }
	}
}
