package io.github.dsheirer.module.decode.p25.message.tsbk2;

import io.github.dsheirer.bits.BinaryMessage;
import io.github.dsheirer.bits.CorrectedBinaryMessage;
import io.github.dsheirer.edac.CRCP25;
import io.github.dsheirer.identifier.IIdentifier;
import io.github.dsheirer.module.decode.p25.message.P25Message;
import io.github.dsheirer.module.decode.p25.reference.DataUnitID;
import io.github.dsheirer.module.decode.p25.reference.Direction;
import io.github.dsheirer.module.decode.p25.reference.Vendor;

import java.util.List;

/**
 * APCO 25 Trunking Signalling Block (TSBK)
 */
public abstract class TSBK2Message extends P25Message
{
    private static final int LAST_BLOCK_FLAG = 0;
    private static final int ENCRYPTION_FLAG = 1;
    private static final int[] OPCODE = {2, 3, 4, 5, 6, 7};
    private static final int[] VENDOR = {8, 9, 10, 11, 12, 13, 14, 15};

    private DataUnitID mDataUnitID;

    /**
     * Constructs a TSBK from the binary message sequence.
     *
     * @param dataUnitID TSBK1/2/3
     * @param message binary sequence
     * @param nac decoded from the NID
     * @param timestamp for the message
     */
    public TSBK2Message(DataUnitID dataUnitID, CorrectedBinaryMessage message, int nac, long timestamp)
    {
        super(message, nac, timestamp);
        mDataUnitID = dataUnitID;

        //The CRC-CCITT can correct up to 1 bit error or detect 2 or more errors.  We mark the message as
        //invalid if the algorithm detects more than 1 correctable error.
        int errors = CRCP25.correctCCITT80(message, 0, 80);
        if(errors > 1)
        {
            setValid(false);
        }
    }

    @Override
    public DataUnitID getDUID()
    {
        return mDataUnitID;
    }

    /**
     * Indicates if this is an encrypted LCW
     */
    public boolean isEncrypted()
    {
        return getMessage().get(ENCRYPTION_FLAG);
    }

    /**
     * Indicates if this is the last TSBK in a sequence (1-3 blocks)
     */
    public boolean isLastBlock()
    {
        return isLastBlock(getMessage());
    }

    /**
     * Indicates if this is the last TSBK in a sequence (1-3 blocks)
     */
    public static boolean isLastBlock(BinaryMessage binaryMessage)
    {
        return binaryMessage.get(LAST_BLOCK_FLAG);
    }

    /**
     * Vendor format for this TSBK.
     */
    public Vendor getVendor()
    {
        return getVendor(getMessage());
    }

    /**
     * Lookup the Vendor format for the specified LCW
     */
    public static Vendor getVendor(BinaryMessage binaryMessage)
    {
        return Vendor.fromValue(binaryMessage.getInt(VENDOR));
    }

    /**
     * Opcode for this TSBK
     */
    public Opcode getOpcode()
    {
        return getOpcode(getMessage(), getDirection());
    }

    /**
     * Opcode for this TSBK
     */
    public static Opcode getOpcode(BinaryMessage binaryMessage, Direction direction)
    {
        return Opcode.fromValue(binaryMessage.getInt(OPCODE), direction);
    }

    /**
     * Direction - inbound (ISP) or outbound (OSP)
     */
    public abstract Direction getDirection();

    /**
     * List of identifiers provided by the message
     */
    public abstract List<IIdentifier> getIdentifiers();

    /**
     * Creates a string with the basic TSBK information
     */
    public String getMessageStub()
    {
        StringBuilder sb = new StringBuilder();

        if(isValid())
        {
            sb.append(getOpcode().getLabel());

            if(isEncrypted())
            {
                sb.append(" ENCRYPTED");
            }
            else
            {
                sb.append(" VENDOR:").append(getVendor().getLabel());
            }
        }
        else
        {
            sb.append("**CRC-FAILED**");
        }

        return sb.toString();
    }
}