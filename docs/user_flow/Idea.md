# Idea
We want to Implement the matching in the Database, in Increment 2.
It would be a lot to implement in the way we intended to.

## Matching to Notes in the Database
The Idea is to Strip down the User Input to the most Important Key word,
so we can match it. 
We have to figure out a way to do this locally, to reduce the Cost on OpenAI.

If we take the User Input and remove all unnecessary Words (e.g. I, We, want)
we can Match them to Keywords in the Database. For example:

Give me the Timetable for CTS 2

would be striped down to:
Timetable, CTS 2

now we could search for these Keywords in the Database.

## OpenAI Response
First we send the User Text with the Current Prompt Tree, 
if that does not return a response we send the Other Trees (excluded the current allready send)
and see if we get a response from that. If no response given, we send a Default error message back
