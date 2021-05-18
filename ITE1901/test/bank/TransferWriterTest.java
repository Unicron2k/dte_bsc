package bank;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class TransferWriterTest {

    private TransferWriter transferWriter;

    @Mock
    private Transfer mockTransfer;

    @Test
    void writeTransfer_CreatesCSVFile() throws IOException {
        Account sourceAccount = new Account(488888888, "Andy", 25000.0);
        Account destinationAccount = new Account(466666666, "Kyrre Kyrre", 50000.0);
        Transfer dummyTransfer = new Transfer(sourceAccount, destinationAccount, 1000);

        BufferedWriter bwTransferWriter = new BufferedWriter(new FileWriter(Bank.TRANSFER_FILE_NAME, true));
        BufferedWriter bwErrorWriter = new BufferedWriter(new FileWriter(Bank.ERROR_FILE_NAME, true));

        TransferWriter twTransferWriter = new TransferWriter(bwTransferWriter, bwErrorWriter);
        twTransferWriter.writeTransfer(dummyTransfer);
        assertTrue(new File(Bank.TRANSFER_FILE_NAME).exists());
    }

    @Test
    void writeTransfer_WritesCorrectData() throws IOException {
        mockTransfer = mock(Transfer.class, RETURNS_DEEP_STUBS);

        when(mockTransfer.getAccountFrom().getAccountNumber()).thenReturn(488888888);
        when(mockTransfer.getAccountTo().getAccountNumber()).thenReturn(466666666);
        when(mockTransfer.getAmount()).thenReturn(1000.00);
        when(mockTransfer.getDate().getTime().getTime()).thenReturn(1574879186095L);

        BufferedWriter mockTransferWriter = mock(BufferedWriter.class);
        BufferedWriter mockErrorWriter = mock(BufferedWriter.class);

        transferWriter = new TransferWriter(mockTransferWriter, mockErrorWriter);
        transferWriter.writeTransfer(mockTransfer);

        verify(mockTransferWriter).write("488888888,466666666,1000.00,2019-11-27 19:26:26\n");
    }

    @AfterEach
    void removeFile() {
        File file = new File("src/Transfer File.csv");
        //file.delete();

    }
}
