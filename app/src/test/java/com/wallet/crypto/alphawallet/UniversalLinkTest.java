package com.wallet.crypto.alphawallet;

import com.wallet.crypto.alphawallet.entity.SalesOrder;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by weiwu on 9/3/18.
 */

public class UniversalLinkTest {
    String link = "https://app.awallet.io/?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAsaK8LsUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWqKEQbyaECakvG8LqLvkhtHQnaVzKznkAD4=";
    int[] indices = new int[] { 0x3e };
    boolean verifySignature(byte[] message, byte[] signature) {
        return false;
    }
    @Test
    public void UniversalLinkShouldBeParsedCorrectly() {
        SalesOrder order = SalesOrder.parseUniversalLink(link);
        assertEquals(new BigInteger("0x2c68af0bb140000", 16), order.priceWei );
        assertEquals(0x5aa28441, order.expiry);
        assertEquals("0xbc9a1026a4bc6f0ba8bbe486d1d09da5732b39e4", order.contractAddress);
        assertArrayEquals(indices, order.tickets);
        assertTrue(verifySignature(order.message, order.signature));
    }

    @Test
    public void UniversalLinkShouldBeGeneratedCorrectly() {
        //SalesOrder order = SalesOrder(......)
    }

}
