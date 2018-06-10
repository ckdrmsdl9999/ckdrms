pragma solidity ^0.4.0;

contract SimpleAuction {
    address public beneficiary;
    uint public auctionStart;
    uint public biddingTime;
    uint256 public reservePrice;
    uint256 public power;
    
 // 경매의 주요 매개변수
 // 시간은 리눅스 시간이 되거나, (1970년1월1일 이후 초)
 // 경매기간을 초로 환산한 것일 수 있다
 //power는 전력량
 //reservePrice는 최소가격

    address public highestBidder;
    uint public highestBid;

    //이전 응찰에 대한 허용된 인출
    mapping(address => uint) pendingReturns;

   //경매 종료 이후는 true, 그 이후에는 어떠한 변화도 허용하지 않는다
    bool ended;

    //변동이 있을 때마다 이벤트가 발생한다
    event HighestBidIncreased(address bidder, uint amount);
    event AuctionEnded(address winner, uint amount);

 //아래는 소위 netspec comment라고 불리는 것으로써
 //슬래시 3개 이후에 적힌다
 //이것은 유저가 트렌젝션을 최종 승인 받기 직전에 보여지게 된다.

 ///‘_biddingTime’의 기간동안 ‘_beneficiery’의 주소로 최대응찰액이 전달되는
 ///간편 공개 경매가 생성된다
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
  /// 이 거래로 전송된 금액을 통해 입찰을 할 수 있다.
 /// 이 경매에서 낙찰받지 못했을 경우에만 돈을 돌려받는다.

    function bid() payable {
     // 매개변수는 필요하지 않다.
     //모든 필요한 정보는 트렌젝션 자체에 있다.
     // payable 키워드는 이더를 받기 위해서 필요하다
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

    //초과금액을 경매참여자에게 인출해준다
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

 //경매를 끝내고 최대금액을 판매자에게 준다.
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

       //상태변경을 위하여 다른 컨트렉트와 연결되는부분..
        if (!beneficiary.send(highestBid))
            throw;
    }
}
