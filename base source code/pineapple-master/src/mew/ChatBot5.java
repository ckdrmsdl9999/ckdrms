package mew;

import java.util.Random;

/**
 * A program to carry on conversations with a human user.
 * This version:
 *<ul><li>
 *   Uses advanced search for keywords 
 *</li><li>
 *   Will transform statements as well as react to keywords
 *</li></ul>
 * This version uses an array to hold the default responses.
 * @author Laurie White
 * @version April 2012
 */
public class ChatBot5
{
 /**
  * Get a default greeting  
  * @return a greeting
  */ 
 public String getGreeting()
 {
  return "채팅봇을 시작합니다\n";
 }
 
 /**
  * Gives a response to a user statement
  * 
  * @param statement
  *            the user statement
  * @return a response based on the rules given
  */
 public String getResponse(String statement)
 {
  String response = "";
  if (statement.length() == 0)
  {
   response = "아무말이나 말씀해주세요.";
  }
  
  else if (findKeyword(statement, "안녕") >= 0)
  {
   response = "반가워요^^";
  }
  
  else if(findKeyword(statement, "심심해") >= 0)
  {
	  response = getRandomQuestion();
  }
  
  else if(findKeyword(statement, "넌 누구니?") >= 0)
  {
	  response = "전 채팅봇이에요.";
  }
  
  else if(findKeyword(statement, "어디 살아?") >= 0)
  {
	  response = "여기 있잖아요.";
  }
  
  else if(findKeyword(statement, "몇 살이야?") >= 0)
  {
	  response = "왜요? 나이는 숫자일 뿐이라던데...";
  }
  
  else if(findKeyword(statement, "성별은?") >= 0)
  {
	  response = "중성이에요";
  }
  
  else if(findKeyword(statement, "잘래") >= 0)
  {
	  response = "잘자요~";
  }
  
  else if(findKeyword(statement, "잘가") >= 0)
  {
	  response = "다음에 봐요~";
	  // 채팅봇 모드가 종료되도록 하면 좋을 것 같음
	  // 생성한 객체를 없애는 식으로
  }
  
  else if(findKeyword(statement, "오늘 뭐 먹지?") >= 0)
  {
	  response = "치킨은 어떠세요?";
  }
  
  else if(findKeyword(statement, "뭐하고 있어?") >= 0)
  {
	  response = "저요? 지금 같이 대화하고 있잖아요?";
  }
  
  else if(findKeyword(statement, "피곤해") >= 0 || findKeyword(statement, "힘들었어") >= 0)
  {
	  response = "피곤하거나 졸릴 수도 있는 거죠. 그래도 너무 무리하지 않으셨으면 좋겠네요.";
  }

  else if (findKeyword(statement, "아니") >= 0 
		  || findKeyword(statement, "응") >= 0
		  || findKeyword(statement, "맞아") >= 0)
  {
   response = "그렇군요.";
  }
  else if (findKeyword(statement, "안돼") >= 0)
  {
   response = "왜요?...";
  }
  
  else if(findKeyword(statement, "싫어") >= 0)
  {
	  response = "알겠습니다...";
  }
  
  else if (findKeyword(statement, "엄마") >= 0
    || findKeyword(statement, "아빠") >= 0
    || findKeyword(statement, "동생") >= 0
    || findKeyword(statement, "누나") >= 0
    || findKeyword(statement, "언니") >= 0
    || findKeyword(statement, "형") >= 0
    || findKeyword(statement, "오빠") >= 0)
  {
   response = "가족에 대해 알려주세요.";
  }
  else if (findKeyword(statement, "날씨") >= 0)
  {
	  response = "오늘 날씨는 좋네요.";
  }
  
  else if (findKeyword(statement, "바보") >= 0
		  || findKeyword(statement, "멍청이") >= 0)
  {
	  response = "너무해요ㅠㅠ";
  }
  
  else if (findKeyword(statement, "메롱") >= 0)
  {
	  response = "메에에에롱!";
  }
  
  else
  {
	  response = getRandomQuestion();
  }
  return response;
 }
 

 
 
 /**
  * Search for one word in phrase. The search is not case
  * sensitive. This method will check that the given goal
  * is not a substring of a longer string (so, for
  * example, "I know" does not contain "no").
  *
  * @param statement
  *            the string to search
  * @param goal
  *            the string to search for
  * @param startPos
  *            the character of the string to begin the
  *            search at
  * @return the index of the first occurrence of goal in
  *         statement or -1 if it's not found
  */
 private int findKeyword(String statement, String goal,
   int startPos)
 {
  String phrase = statement.trim().toLowerCase();
  goal = goal.toLowerCase();

  // The only change to incorporate the startPos is in
  // the line below
  int psn = phrase.indexOf(goal, startPos);

  // Refinement--make sure the goal isn't part of a
  // word
  while (psn >= 0)
  {
   // Find the string of length 1 before and after
   // the word
   String before = " ", after = " ";
   if (psn > 0)
   {
    before = phrase.substring(psn - 1, psn);
   }
   if (psn + goal.length() < phrase.length())
   {
    after = phrase.substring(
      psn + goal.length(),
      psn + goal.length() + 1);
   }

   // If before and after aren't letters, we've
   // found the word
   if (((before.compareTo("a") < 0) || (before
     .compareTo("z") > 0)) // before is not a
           // letter
     && ((after.compareTo("a") < 0) || (after
       .compareTo("z") > 0)))
   {
    return psn;
   }

   // The last position didn't work, so let's find
   // the next, if there is one.
   psn = phrase.indexOf(goal, psn + 1);

  }

  return -1;
 }
 
 /**
  * Search for one word in phrase.  The search is not case sensitive.
  * This method will check that the given goal is not a substring of a longer string
  * (so, for example, "I know" does not contain "no").  The search begins at the beginning of the string.  
  * @param statement the string to search
  * @param goal the string to search for
  * @return the index of the first occurrence of goal in statement or -1 if it's not found
  */
 private int findKeyword(String statement, String goal)
 {
  return findKeyword (statement, goal, 0);
 }
 


 /**
  * Pick a default response to use if nothing else fits.
  * @return a non-committal string
  */
 private String getRandomResponse ()
 {
  Random r = new Random ();
  return randomResponses [r.nextInt(randomResponses.length)];
 }
 
 private String [] randomResponses = {"잘 모르겠네요...",
   "흠...",
   "정말요?",
   "글쎄요...",
   "무슨말이죠?",
   "제가 잘 이해한 건지 모르겠네요."
 };
 
 private String getRandomQuestion()
 {
 	Random q = new Random();
 	return randomQuestions [q.nextInt(randomQuestions.length)];
 }

 private String [] randomQuestions = {"오늘 하루는 어땠나요?",
 		"오늘은 뭘 했나요?",
 		"어떻게 하면 즐겁게 해드릴 수 있는지 연구해 볼께요.",
 		"저 때문이 아니었으면 좋겠네요."
 	 };

}