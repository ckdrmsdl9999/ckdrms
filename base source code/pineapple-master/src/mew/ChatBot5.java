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
  return "ä�ú��� �����մϴ�\n";
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
   response = "�ƹ����̳� �������ּ���.";
  }
  
  else if (findKeyword(statement, "�ȳ�") >= 0)
  {
   response = "�ݰ�����^^";
  }
  
  else if(findKeyword(statement, "�ɽ���") >= 0)
  {
	  response = getRandomQuestion();
  }
  
  else if(findKeyword(statement, "�� ������?") >= 0)
  {
	  response = "�� ä�ú��̿���.";
  }
  
  else if(findKeyword(statement, "��� ���?") >= 0)
  {
	  response = "���� ���ݾƿ�.";
  }
  
  else if(findKeyword(statement, "�� ���̾�?") >= 0)
  {
	  response = "�ֿ�? ���̴� ������ ���̶����...";
  }
  
  else if(findKeyword(statement, "������?") >= 0)
  {
	  response = "�߼��̿���";
  }
  
  else if(findKeyword(statement, "�߷�") >= 0)
  {
	  response = "���ڿ�~";
  }
  
  else if(findKeyword(statement, "�߰�") >= 0)
  {
	  response = "������ ����~";
	  // ä�ú� ��尡 ����ǵ��� �ϸ� ���� �� ����
	  // ������ ��ü�� ���ִ� ������
  }
  
  else if(findKeyword(statement, "���� �� ����?") >= 0)
  {
	  response = "ġŲ�� �����?";
  }
  
  else if(findKeyword(statement, "���ϰ� �־�?") >= 0)
  {
	  response = "����? ���� ���� ��ȭ�ϰ� ���ݾƿ�?";
  }
  
  else if(findKeyword(statement, "�ǰ���") >= 0 || findKeyword(statement, "�������") >= 0)
  {
	  response = "�ǰ��ϰų� ���� ���� �ִ� ����. �׷��� �ʹ� �������� ���������� ���ڳ׿�.";
  }

  else if (findKeyword(statement, "�ƴ�") >= 0 
		  || findKeyword(statement, "��") >= 0
		  || findKeyword(statement, "�¾�") >= 0)
  {
   response = "�׷�����.";
  }
  else if (findKeyword(statement, "�ȵ�") >= 0)
  {
   response = "�ֿ�?...";
  }
  
  else if(findKeyword(statement, "�Ⱦ�") >= 0)
  {
	  response = "�˰ڽ��ϴ�...";
  }
  
  else if (findKeyword(statement, "����") >= 0
    || findKeyword(statement, "�ƺ�") >= 0
    || findKeyword(statement, "����") >= 0
    || findKeyword(statement, "����") >= 0
    || findKeyword(statement, "���") >= 0
    || findKeyword(statement, "��") >= 0
    || findKeyword(statement, "����") >= 0)
  {
   response = "������ ���� �˷��ּ���.";
  }
  else if (findKeyword(statement, "����") >= 0)
  {
	  response = "���� ������ ���׿�.";
  }
  
  else if (findKeyword(statement, "�ٺ�") >= 0
		  || findKeyword(statement, "��û��") >= 0)
  {
	  response = "�ʹ��ؿ�Ф�";
  }
  
  else if (findKeyword(statement, "�޷�") >= 0)
  {
	  response = "�޿�������!";
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
 
 private String [] randomResponses = {"�� �𸣰ڳ׿�...",
   "��...",
   "������?",
   "�۽��...",
   "����������?",
   "���� �� ������ ���� �𸣰ڳ׿�."
 };
 
 private String getRandomQuestion()
 {
 	Random q = new Random();
 	return randomQuestions [q.nextInt(randomQuestions.length)];
 }

 private String [] randomQuestions = {"���� �Ϸ�� �����?",
 		"������ �� �߳���?",
 		"��� �ϸ� ��̰� �ص帱 �� �ִ��� ������ ������.",
 		"�� ������ �ƴϾ����� ���ڳ׿�."
 	 };

}