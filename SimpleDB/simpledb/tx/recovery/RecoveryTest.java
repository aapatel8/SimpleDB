
package simpledb.tx.recovery;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class RecoveryTest {
	public static void main(String[] args) {
		
		SimpleDB.init("simpleDB");
		BufferMgr bm = new SimpleDB().bufferMgr();
		
		Block blk1 = new Block("filename", 1);
		Buffer buff = bm.pin(blk1);
		
		RecoveryMgr rm1 = new RecoveryMgr(1);
		RecoveryMgr rm2 = new RecoveryMgr(2);
		//RecoveryMgr rm3 = new RecoveryMgr(3);
		
		int lsn = rm1.setInt(buff, 4, 5);
		buff.setInt(4, 5, 1, lsn);
		System.out.println(lsn);
		lsn = rm1.setInt(buff, 4, 10);
		buff.setInt(4, 10, 1, lsn);
		System.out.println(lsn);
		rm1.commit();
		bm.unpin(buff);
		bm.flushAll(1);
		
		Block blk2 = new Block("filename2", 2);
		buff = bm.pin(blk2);
		lsn = rm2.setInt(buff, 4, 15);
		buff.setInt(4, 15, 2, lsn);
		Block blk3 = new Block("filename3", 3);
		buff = bm.pin(blk3);
		lsn = rm2.setString(buff, 0, "hello");
		buff.setString(0, "hello", 2, lsn);
		bm.unpin(buff);
		bm.flushAll(2);
		
		
		//rm1.recover();
//		
//		lsn =  rm.setInt(buff, 4, 1235);
//		buff.setInt(4, 1235, 123, lsn);
		//bm.flushAll(123);
		
		LogRecordIterator it = new LogRecordIterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		//rm.commit();
		//rm.recover();
		
		
	}
}
