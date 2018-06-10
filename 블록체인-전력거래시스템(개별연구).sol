pragma solidity ^0.4.0;

contract SimpleAuction {
    address public beneficiary;
    uint public auctionStart;
    uint public biddingTime;
    uint256 public reservePrice;
    uint256 public power;
    
 // ����� �ֿ� �Ű�����
 // �ð��� ������ �ð��� �ǰų�, (1970��1��1�� ���� ��)
 // ��űⰣ�� �ʷ� ȯ���� ���� �� �ִ�
 //power�� ���·�
 //reservePrice�� �ּҰ���

    address public highestBidder;
    uint public highestBid;

    //���� ������ ���� ���� ����
    mapping(address => uint) pendingReturns;

   //��� ���� ���Ĵ� true, �� ���Ŀ��� ��� ��ȭ�� ������� �ʴ´�
    bool ended;

    //������ ���� ������ �̺�Ʈ�� �߻��Ѵ�
    event HighestBidIncreased(address bidder, uint amount);
    event AuctionEnded(address winner, uint amount);

 //�Ʒ��� ���� netspec comment��� �Ҹ��� �����ν�
 //������ 3�� ���Ŀ� ������
 //�̰��� ������ Ʈ�������� ���� ���� �ޱ� ������ �������� �ȴ�.

 ///��_biddingTime���� �Ⱓ���� ��_beneficiery���� �ּҷ� �ִ��������� ���޵Ǵ�
 ///���� ���� ��Ű� �����ȴ�
    function SimpleAuction(
        uint _biddingTime,
        address _beneficiary,
        uint poweramount,
        uint minprice
    ) {
        beneficiary = _beneficiary;
        auctionStart = now;
        biddingTime = _biddingTime;
        power=poweramount;
        reservePrice=minprice;
    }
    

//function powerview() constant returns (uint) {

     //   return power;

  //  }
  /// �� �ŷ��� ���۵� �ݾ��� ���� ������ �� �� �ִ�.
 /// �� ��ſ��� �������� ������ ��쿡�� ���� �����޴´�.

    function bid() payable {
     // �Ű������� �ʿ����� �ʴ�.
     //��� �ʿ��� ������ Ʈ������ ��ü�� �ִ�.
     // payable Ű����� �̴��� �ޱ� ���ؼ� �ʿ��ϴ�
        if (now > auctionStart + biddingTime) {
            // Revert the call if the bidding
            // period is over.
            throw;
        }
        if (msg.value <= highestBid) {
            // If the bid is not higher, send the
            // money back.
            throw;
        }
           if (msg.value < reservePrice) {
            // If the bid is not higher, send the
            // money back.
            throw;
        }
        
        if (highestBidder != 0) {
            // Sending back the money by simply using
            // highestBidder.send(highestBid) is a security risk
            // because it can be prevented by the caller by e.g.
            // raising the call stack to 1023. It is always safer
            // to let the recipient withdraw their money themselves.
            pendingReturns[highestBidder] += highestBid;
        }
        highestBidder = msg.sender;
        highestBid = msg.value;
        HighestBidIncreased(msg.sender, msg.value);
    }

    //�ʰ��ݾ��� ��������ڿ��� �������ش�
    function withdraw() returns (bool) {
        var amount = pendingReturns[msg.sender];
        if (amount > 0) {
          
            pendingReturns[msg.sender] = 0;

            if (!msg.sender.send(amount)) {
     
                pendingReturns[msg.sender] = amount;
                return false;
            }
        }
        return true;
    }

 //��Ÿ� ������ �ִ�ݾ��� �Ǹ��ڿ��� �ش�.
    function auctionEnd() {
        // It is a good guideline to structure functions that interact
        // with other contracts (i.e. they call functions or send Ether)
        // into three phases:
        // 1. checking conditions
        // 2. performing actions (potentially changing conditions)
        // 3. interacting with other contracts
        // If these phases are mixed up, the other contract could call
        // back into the current contract and modify the state or cause
        // effects (ether payout) to be perfromed multiple times.
        // If functions called internally include interaction with external
        // contracts, they also have to be considered interaction with
        // external contracts.

        // 1. Conditions
        if (now <= auctionStart + biddingTime)
            throw; // auction did not yet end
        if (ended)
            throw; // this function has already been called

        // 2. Effects
        ended = true;
        AuctionEnded(highestBidder, highestBid);

       //���º����� ���Ͽ� �ٸ� ��Ʈ��Ʈ�� ����Ǵºκ�..
        if (!beneficiary.send(highestBid))
            throw;
    }
}
