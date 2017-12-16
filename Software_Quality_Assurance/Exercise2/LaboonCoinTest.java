import static org.junit.Assert.*;

import org.junit.*;

public class LaboonCoinTest {

    // Re-usable LaboonCoin reference.
    LaboonCoin _l;

    // Create a new LaboonCoin instance before each test.
    @Before
    public void setup() {
	_l = new LaboonCoin();
    }
    
    // Assert that creating a new LaboonCoin instance
    // does not return a null reference
    @Test
    public void testLaboonCoinExists() {
	assertNotNull(_l);
    }

    // Creating a block String with valid data should return
    // a valid block.  This includes printing data out
    // normally, the previous hash and current hash as a hex
    // representations of themself, and the nonce in hex
    // repretentation.  Values should be delimited by
    // pipes.
    @Test
    public void testCreateBlockValid() {
	int prevHash = 0x0;
	int nonce = 0x16f2;
	int hash = 0x545ac;
	String validBlock = _l.createBlock("kitten", prevHash, nonce, hash);
	assertEquals("kitten|00000000|000016f2|000545ac", validBlock);
    }

    // Trying to represent a blockchain which has no blocks
    // in it should just return an empty string.
    @Test
    public void testGetBlockChainNoElements() {
	// By default, _l.blockchain has no elements in it.
	// So we can just test it immediately.
	String blockChain = _l.getBlockChain();
	assertEquals("", blockChain);
    }
    

    // Viewing the blockchain as a full string which has valid
    // elements should include all of the elements.  Note that the
    // final element DOES have a trailing \n!
    @Test
    public void testGetBlockChainElements() {
	_l.blockchain.add("TESTBLOCK1|00000000|000010e9|000a3cd8");
	_l.blockchain.add("TESTBLOCK2|000a3cd8|00002ca8|0008ff30");
	_l.blockchain.add("TESTBLOCK3|0008ff30|00002171|0009f908");
	String blockChain = _l.getBlockChain();
	assertEquals("TESTBLOCK1|00000000|000010e9|000a3cd8\nTESTBLOCK2|000a3cd8|00002ca8|0008ff30\nTESTBLOCK3|0008ff30|00002171|0009f908\n", blockChain);
    }
	    
    // TODO - PUT YOUR SIX TESTS HERE
    //test the hash value if null value is passed in
    @Test
    public void testHashNull(){
        assertEquals(_l.hash(null),0x00989680);
    }
     //test the hash value if empty String is passed in
    @Test
    public void testHashEmpty(){
        assertEquals(_l.hash(""),0x00989680);
    }
     //test the hash value if "boo" is passed in
    @Test
    public void testHashBoo(){
        assertEquals(_l.hash("boo"),0x551fda32);
    }
    //test the validity of hash value of "" under difficulty 2
    //should return true
    @Test
    public void testValidHash2Empty(){
        assertTrue(_l.validHash(2, 0x00989680));
    }
    //test the validity of hash value of "boo" under difficulty 3
    //shoudl return false
    @Test
    public void testValidHash3Boo(){
        assertFalse(_l.validHash(3, 0x551fda32));
    }
    //test the validity of hash value of "boo" under difficulty 0
    //shoudl return false
    @Test
    public void testValidHash0Boo(){
        assertTrue(_l.validHash(0, 0x551fda32));
    }

}
