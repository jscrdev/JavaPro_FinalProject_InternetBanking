package org.example.springbank.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.example.springbank.enums.TransactionType;

import java.util.Date;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Transactions")
public class Transaction extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "UTC")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Account receiver;

    @Column(nullable = false)
    private double senderAmount;

    @Column(nullable = false)
    private double receiverAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    public Transaction(
            Account senderAccount,
            Account receiverAccount,
            double senderAmount,
            double receiverAmount,
            TransactionType type) {
        this.date = new Date();
        this.sender = senderAccount;
        this.receiver = receiverAccount;
        this.senderAmount = senderAmount;
        this.receiverAmount = receiverAmount;
        this.type = type;
    }

    public void updateSenderAmount(double senderAmount) {
        this.senderAmount += senderAmount;
    }

    @Override
    public String toString() {
        return "Transaction{"
                + "senderAccount="
                + sender
                + ", "
                + "receiverAccount="
                + receiver
                + ", "
                + "senderAmount="
                + senderAmount
                + ", "
                + "type="
                + type
                + "}";
    }
}
