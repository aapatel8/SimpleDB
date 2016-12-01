
package simpledb.tx.recovery;

import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class RecoveryTest {
	public static void main(String[] args) {
		SimpleDB.init("simpleDB");
		Block blk1 = new Block("filename", 1);
		RecoveryMgr rm = new RecoveryMgr(123);
		rm.commit();
		rm.recover();
	}
}
