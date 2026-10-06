# Communication Frameworks — Complete Guide

This file explains every framework used in this course. For each one you will find:
- What it is and why it exists
- The real problem it solves
- How to use it step by step
- A before/after example
- Common mistakes to avoid

Read this file once at the start. Then return to specific sections as they come up in each phase.

---

## Part 1 — Frameworks for Structure & Writing

These are used in Phase 1. They solve the most common communication problem: people write and speak in the order they think, not in the order the reader needs.

---

### BLUF — Bottom Line Up Front

#### What is it?
BLUF is a principle where you state your main point — the conclusion, the ask, the answer — in the very first sentence. Everything else comes after.

The name comes from the US military, where briefings must lead with the most critical information because commanders have no time for context-first storytelling. The same logic applies to every professional message.

#### The problem it solves
Most people write like detectives writing their own mystery novel. They lay out all the clues first — the background, the context, the situation, the history — and only reveal the conclusion at the end. The reader has to work through everything to find out what you actually want from them.

This is exhausting for the reader. It also creates a dangerous gap: if someone reads only the first paragraph (which most people do), they leave knowing nothing.

**Why you do it wrong by default:** We were taught in school to build up to a point — introduction, body, conclusion. That structure works for essays where the goal is to persuade a reader who has agreed to give you their full attention. It does not work for business communication where people are distracted, busy, and skimming.

#### How to use it

**Step 1.** Write your message the normal way — the way it comes out of your head.

**Step 2.** Find the sentence that contains the actual answer, decision, or request. It is usually near the end.

**Step 3.** Move that sentence to the very beginning.

**Step 4.** Read the result. The first sentence should now make complete sense without reading anything else.

#### Before / After Example

**Before (context-first):**
> "Hi team, as you know we have been evaluating the three vendors for the past two weeks. We looked at pricing, support quality, and integration complexity. After reviewing all the data and speaking with the technical team, I wanted to share what we found. Based on everything, I think we should go with Vendor B."

The reader reaches the decision only in the last sentence. If they stop at sentence two, they know nothing.

**After (BLUF):**
> "We should go with Vendor B. After two weeks of evaluation across pricing, support quality, and integration complexity, they came out ahead on all three. Details below."

Now the first sentence delivers everything. The rest is supporting material for those who want it.

#### What to pay attention to

- **Do not confuse the first sentence with the first topic.** "I wanted to reach out about the project" is not BLUF. The first sentence must contain the actual answer or ask.
- **BLUF does not mean skipping context.** You still include background — just after the point, not before it.
- **For emails:** The subject line is also part of BLUF. "Meeting on Thursday" is weak. "Meeting Thursday 3pm — your approval needed" is BLUF in the subject line.
- **For Slack messages:** One sentence is often enough. "Deployment postponed to Friday — production issue found, fix by tomorrow" is a complete message.

#### Practice trigger
Every time you write a message longer than 3 sentences, ask yourself: "If someone reads only the first sentence, do they know what I need from them?" If the answer is no, apply BLUF.

---

### Pyramid Principle

#### What is it?
The Pyramid Principle is a structure for organizing written documents and presentations. It was developed by Barbara Minto at McKinsey in the 1970s and is still the gold standard for business writing worldwide. The idea: state your conclusion first, then support it with arguments, then support each argument with evidence.

Think of it as an upside-down triangle or a pyramid viewed from above. The single point is at the top. Everything below supports it.

```
         [Your conclusion]
        /        |        \
  [Arg 1]    [Arg 2]    [Arg 3]
     |           |          |
[Evidence]  [Evidence]  [Evidence]
```

#### The problem it solves
Most documents fail because they are organized around the author's thinking process — "first I researched X, then I found Y, then I realized Z." The reader does not care about your process. They care about your conclusion and whether they can trust it.

The Pyramid forces you to separate what you think from why you think it. Your reader gets the answer immediately and then decides how deep they want to go into the evidence.

#### How to use it

**Step 1. Decide your conclusion first.**
Before writing anything, write a single sentence that answers the question your document is addressing. This is the top of your pyramid. Example: "We should expand into the German market in Q3."

**Step 2. Identify 2–4 main arguments that support it.**
These are not sub-topics or sections — they are reasons your conclusion is true. Example arguments: "Demand is high," "Competition is low," "We have the operational capacity."

**Step 3. Find evidence for each argument.**
Data, examples, quotes, analysis. Each piece of evidence should support exactly one argument. If it supports none, cut it.

**Step 4. Write top-down.**
Start with the conclusion. Then present the arguments. Then the evidence under each.

#### Before / After Example

**Before (bottom-up):**
> "We looked at the German market and found that search volume for our product category grew 34% last year. We also see that there are only two local competitors and neither has strong brand recognition. Our logistics team confirmed we can handle EU distribution with current infrastructure. Therefore, we should consider expanding to Germany."

**After (Pyramid):**
> "We should expand into Germany in Q3. Three factors make this the right time: strong demand growth (34% YoY search increase), weak local competition (two players with low brand recognition), and operational readiness (logistics team confirmed EU distribution with current infrastructure)."

#### What to pay attention to

- **Your 2–4 arguments should not overlap.** If two arguments are saying the same thing in different words, merge them.
- **Each argument should be a complete statement, not a topic.** "Market demand" is a topic. "Market demand is growing faster than supply" is an argument.
- **The pyramid works for verbal communication too.** In meetings, lead with your recommendation, then say "for three reasons," then name them.

---

### PREP — Point, Reason, Example, Point

#### What is it?
PREP is a framework for structuring any spontaneous spoken answer. It gives you a four-part shape to follow when you are put on the spot: state your Point, give your Reason, provide an Example, and restate your Point.

It is designed for situations where you cannot prepare — someone asks you a question in a meeting, you are put on the spot in a job interview, someone asks your opinion in a conversation.

#### The problem it solves
When people answer questions without a structure, they either say too little ("I think it's fine") or too much (they talk for two minutes, lose the thread, and end somewhere unrelated to the question). PREP gives your brain a track to follow so your answer always has a beginning, a middle, and an end.

#### How to use it

Say someone asks: "What do you think about switching to microservices?"

**Point** — state your position directly. "I think we should wait another 6 months before switching."

**Reason** — explain why in one or two sentences. "We don't have the team size to manage the operational overhead yet. Microservices multiply the number of moving parts, and with our current headcount that becomes a liability."

**Example** — make it concrete with a real or hypothetical case. "In our last incident, it took us 4 hours to find the root cause across 3 services. With 15 services that would be even harder."

**Point** — close by restating your position, often with an action. "So my recommendation is to hold, scale the team first, and revisit in Q4."

Total time: 30–90 seconds. Completely structured. No rambling.

#### What to pay attention to

- **The Point comes first.** Never open with context or background when using PREP. State the position immediately. This feels uncomfortable at first because we are trained to "ease into" opinions. Do it anyway.
- **The Example must be concrete.** "In my experience it works" is not an example. A specific situation, number, or story is an example.
- **You do not have to use all four parts for short questions.** For casual conversation, Point + Reason is enough. Full PREP is for professional or high-stakes situations.
- **Practice PREP on trivial questions first.** "What should we have for dinner?" → Point: "Let's get Thai." Reason: "We haven't had it in two weeks and it's fast." Example: "Last time we ordered from Lotus it took 20 minutes." Point: "Thai tonight, Lotus specifically." You are wiring a habit.

---

### What / So What / Now What

#### What is it?
A three-part framework for any status update, briefing, or situation report. It answers the three questions every listener always has: what is happening, why should I care, and what do we do next.

#### The problem it solves
Status updates are the most common form of professional communication and the most commonly done badly. People either give too much detail with no recommendation, or jump to a recommendation with no context. This framework forces you to be complete.

#### How to use it

**What** — describe the situation factually. No judgment, no interpretation. Just what happened or what is true. "The API latency increased from 120ms to 850ms starting at 14:00 today."

**So What** — explain why this matters to the listener. Connect it to something they care about: users, revenue, deadlines, risk. "At 850ms, 23% of users are seeing timeouts. This is affecting checkout flow and will likely impact today's revenue figures."

**Now What** — state the recommended action or next step clearly. "We are rolling back the deployment from 13:45. Expected resolution in 30 minutes. I'll send confirmation when it's done."

#### What to pay attention to

- **Do not skip "So What."** It is the most commonly omitted part. You know why it matters — your listener may not. Spell it out.
- **"Now What" should be a decision or a recommendation, not a vague next step.** "We'll look into it" is not Now What. "We are rolling back now, decision by 17:00 on root cause" is Now What.
- **Works verbally in meetings.** You can use this to answer "what's the status?" in any standup or check-in.

---

## Part 2 — Frameworks for Conversations

Used in Phase 2. These help you have better conversations, not just better documents.

---

### TALK — Topics, Asking, Levity, Kindness

#### What is it?
The TALK framework was developed by Alison Wood Brooks, a behavioral science professor at Harvard Business School, and published in her 2026 book *Talk: The Science of Conversation*. It is the result of years of research into what makes conversations go well or badly. The framework gives you four levers to control in any conversation.

#### The problem it solves
Most people approach conversations reactively — they respond to whatever comes up, they say what comes to mind, and they hope it goes well. Research shows that conversations have predictable patterns of success and failure. TALK gives you a model to influence those patterns intentionally.

#### The four elements

**T — Topics**
The research shows that people who prepare 2–3 topics before a conversation — even a casual one — are rated as significantly more interesting and engaging by the other person afterward. You do not have to use all your topics. Just having them removes the anxiety of silence and opens more directions for the conversation.

How to use it: Before any meaningful conversation — a work meeting, a dinner, a first date, a call with a friend you haven't spoken to in a while — write down 2–3 things you genuinely want to know or talk about. They do not have to be clever. "How did their trip go" or "what do they think about the new team structure" is enough.

**A — Asking**
Brooks' research has a counterintuitive finding: people consistently underestimate how many questions they should ask in a conversation. There is almost no upper limit — the more questions you ask, the more the other person enjoys the conversation and perceives you as a good communicator.

Most people do the opposite. They wait for a question to end so they can share their own opinion or story. The shift: become genuinely curious about the other person's answer before thinking about your own.

How to use it: In your next conversation, try to ask at least one follow-up question on every topic before introducing your own view. "What happened next?" / "How did that feel?" / "What made you decide that?" are universally useful follow-up questions.

**L — Levity**
Levity means lightness, humor, and ease. Research shows that conversations with moments of levity are remembered as better conversations even when the topics were serious. You do not need to be funny. You need to not be artificially serious.

How to use it: Notice when you are suppressing a light observation or a small joke because it "doesn't fit" the professional context. Usually it fits better than you think. The simplest version of levity is genuine laughter when something is actually funny, rather than forcing a neutral face to signal seriousness.

**K — Kindness**
Kindness here is not politeness — it is active, real listening. Brooks distinguishes between pretending to listen (nodding, saying "mm-hmm", waiting for your turn) and actually tracking what the other person is saying and responding to it specifically.

How to use it: After the other person finishes speaking, make your first response about something they just said — repeat a word, ask about a detail they mentioned, or reflect back what you heard. This proves you were actually listening, not just waiting.

#### What to pay attention to

- **You do not have to apply all four in every conversation.** Start with one — most people get the most immediate benefit from Asking more questions.
- **Topics does not mean an agenda.** You are not interviewing someone. The topics are just starting points you can drop into the conversation when there is a lull.

---

### 3 Conversation Types — Duhigg

#### What is it?
Charles Duhigg, in *Supercommunicators* (2024), identifies that every conversation is actually one of three types: Practical, Emotional, or Social. The single biggest cause of miscommunication is when two people are having different types of conversation without realizing it.

#### The three types

**Practical** — "What is this about? What do we do?"
The person wants to solve a problem, make a decision, or exchange information. They want facts, options, recommendations.

**Emotional** — "How do I feel? Do you understand me?"
The person wants to be heard and understood. They are not (yet) asking for a solution. If you give them a solution, they feel dismissed.

**Social** — "Who are we to each other? Where do I belong?"
The person is negotiating identity, trust, or relationship. Common in team dynamics, feedback conversations, and moments of conflict about roles.

#### The problem it solves
Imagine your partner comes home and says: "My boss criticized my presentation in front of the whole team today." You respond: "You should talk to HR about it." You are having a Practical conversation. They are having an Emotional one. They feel unseen. The mismatch is the conflict — not the content.

#### How to use it

Before responding to anything with emotional weight, silently ask: "What type of conversation does this person need right now?"

- If Practical: give information, options, recommendations.
- If Emotional: acknowledge feelings first. "That sounds really frustrating." Do not offer solutions until they ask.
- If Social: slow down, pay attention to what is really being communicated about the relationship, trust, or belonging.

You can also ask directly: "Do you want to talk through it, or do you need help figuring out what to do?"

#### What to pay attention to

- **The type can shift mid-conversation.** Someone may start Emotional and shift to Practical once they feel heard. Follow the shift.
- **Most men default to Practical.** This is the most common miscommunication pattern in both professional and personal relationships. When in doubt, lead with Emotional acknowledgment first.

---

## Part 3 — Frameworks for Hard Conversations & Feedback

Used in Phase 3.

---

### SBI — Situation, Behavior, Impact

#### What is it?
SBI is a model for giving feedback. It was developed by the Center for Creative Leadership and is used in executive coaching and leadership programs worldwide. It structures feedback so that it is specific, observable, and about impact — not character, intention, or personality.

#### The problem it solves
Most feedback fails for one of two reasons: it is too vague ("you need to communicate better") or it is a character judgment ("you're not reliable"). Vague feedback cannot be acted on. Character judgments trigger defensiveness and shut down the conversation. SBI eliminates both problems by keeping feedback tied to observable facts and specific effects.

#### How to use it

**Situation** — name the specific moment. Not "in general" or "sometimes" — a real, specific event.
"In yesterday's sprint planning meeting..."

**Behavior** — describe what you observed. Exactly what you saw or heard. Nothing about intent or interpretation.
"...when you said the estimate was two days before asking the team..."

**Impact** — describe the concrete effect on you, the team, or the outcome. Not your judgment — the actual result.
"...two engineers looked confused and we spent 20 minutes revisiting estimates that had already been agreed."

Full example: "In yesterday's sprint planning, when you set the estimate before asking the team, two engineers looked confused and we lost 20 minutes going back over estimates we had already agreed on."

#### What to pay attention to

- **Situation must be specific.** "In meetings" is not a situation. "In the planning call on Tuesday at 10am" is.
- **Behavior must be observable.** "When you were dismissive" is an interpretation. "When you said 'that's not relevant' and moved on" is observable.
- **Impact must be real, not hypothetical.** "This could hurt the team" is weak. "Three team members told me afterward they weren't sure what to work on" is real.
- **Do not add "because you always..." or "this is a pattern."** One SBI at a time. Adding patterns turns specific feedback into a global indictment.
- **SBI works for positive feedback too.** "In today's demo, when you paused to ask the client if they had questions, they opened up about three concerns we hadn't heard before. That saved us a week of rework."

---

### NVC — Nonviolent Communication

#### What is it?
Developed by psychologist Marshall Rosenberg, Nonviolent Communication (NVC) is a framework for expressing your needs and hearing others' needs without blame, judgment, or demand. It is particularly useful in emotionally charged situations with people you have ongoing relationships with — family, partners, close colleagues.

#### The four steps

**Observation** — describe what you saw or heard without evaluation or interpretation. The test: could a camera have recorded it?
"When I saw the report had not been updated by the deadline..."

**Feeling** — state your actual emotion. Not "I feel that you..." (that is a thought, not a feeling). One-word emotions: worried, frustrated, disappointed, embarrassed, relieved.
"...I felt frustrated and worried..."

**Need** — identify the universal human need behind your feeling. Common needs: reliability, respect, clarity, connection, autonomy, safety.
"...because I need to be able to count on the timeline to coordinate with the client."

**Request** — make a specific, positive, actionable request. Not a demand (demands imply punishment if refused). A genuine request with room to say no.
"Would you be willing to send me a message by 5pm if you know the report will be late?"

#### What to pay attention to

- **Feelings are not thoughts.** "I feel that you don't care" is a thought (about the other person). "I feel hurt" is a feeling.
- **Needs are universal.** Everyone has needs for reliability, connection, and respect. Naming a need is not weakness — it is precision. It tells the other person exactly what would help.
- **A request is not a demand.** If the other person says no and you get angry, it was a demand. Real requests leave room for "no, but here's what I can do."
- **NVC is not natural at first.** It feels stiff and formulaic initially. Use it anyway. The structure forces you to slow down and locate what you actually feel and need, which is the hardest and most valuable part.

---

### Fisher's 3-Part System — Control, Confidence, Connect

#### What is it?
Jefferson Fisher is a trial lawyer who has spent 15 years in high-pressure adversarial conversations. His system, from *The Next Conversation* (2025), is built around one core idea: the goal of any difficult conversation is not to win — it is to understand and be understood. The three parts give you a sequence to follow.

#### Step 1 — Say It with Control
Control means your emotional state, not the other person. In any heated moment, your nervous system will want to react — faster, louder, more aggressive. Every reaction done in that state makes things worse.

Control is: slow down, lower your voice, pause before responding. The pause is not weakness — it signals that you are choosing your response, not reacting. People respect it even when they do not realize it.

Practical tool: when you feel the urge to react, take one breath and start your sentence at a slower pace than feels natural.

#### Step 2 — Say It with Confidence
Confidence is not aggression. It means speaking clearly, directly, and without apologizing for your position. Confident communication is: short sentences, no hedging, no over-explaining.

Common confidence killers to eliminate:
- "I might be wrong, but..." — remove it
- "Sorry to bother you, but..." — remove it
- "This is probably a stupid question, but..." — remove it
- Upward inflection at the end of statements (making statements sound like questions)

Confident does not mean harsh. "I disagree and here is why" is confident. "You are completely wrong" is aggressive. The difference is that confident communication is about your position, not about attacking theirs.

#### Step 3 — Say It to Connect
Connection is the goal. Even in a disagreement, the aim is for both people to leave understanding each other better than before — not necessarily agreeing, but understanding.

To connect: acknowledge what the other person said before you respond to it. You do not have to agree. "I understand why you see it that way" or "That makes sense from your position" costs you nothing and opens the other person up.

Fisher's key insight: most arguments are not about the stated topic. They are about someone feeling unseen or dismissed. If you make people feel heard, most arguments dissolve on their own.

---

### Tactical Empathy — Voss

#### What is it?
Developed by Chris Voss, former FBI lead hostage negotiator, in *Never Split the Difference*. Tactical empathy is a set of listening and responding tools designed to make the other person feel deeply understood. When people feel understood, they become more open, more honest, and more willing to cooperate.

#### Three main tools

**Mirroring**
Repeat the last 2–3 words the other person said, as a question, with a slightly upward inflection. That is the entire technique.

They say: "The problem is we just don't have the budget for this."
You say: "Don't have the budget?"

This sounds absurdly simple. It works because it signals you were listening, it invites them to keep talking, and it keeps you out of the conversation long enough to actually hear what they are saying.

**Labeling**
Name the emotion you think the other person is experiencing. Start with "It sounds like..." or "It seems like..." or "It feels like..."

"It sounds like you're worried this won't be ready in time."
"It seems like there's some frustration with how this was handled."

You do not need to be exactly right. Even an imprecise label makes the other person feel seen and prompts them to correct or confirm — both of which give you more information.

**Calibrated Questions**
Questions that start with "How" or "What" — never "Why" (which sounds accusatory) and never closed yes/no questions. These questions require the other person to think and engage.

"How am I supposed to work with that timeline?"
"What would make this work for your team?"
"How do we solve this?"

These questions do two things: they give you information, and they make the other person feel respected as a participant in finding the solution rather than on the receiving end of a demand.

---

## Quick Reference — All Frameworks

| Framework | Phase | One-line summary |
|---|---|---|
| **BLUF** | 1 | State the point in the first sentence, always |
| **Pyramid Principle** | 1 | Conclusion first, then arguments, then evidence |
| **PREP** | 1–2 | Point → Reason → Example → Point for any spoken answer |
| **What / So What / Now What** | 1–2 | What happened → why it matters → what to do |
| **TALK** | 2 | Prepare Topics, Ask more, find Levity, practice Kindness |
| **3 Conversation Types** | 3 | Identify: Practical / Emotional / Social before responding |
| **Fisher's 3-Part** | 3 | Control your state → speak confidently → aim to connect |
| **SBI** | 3 | Situation + observable Behavior + concrete Impact |
| **NVC** | 3 | Observation + Feeling + Need + Request |
| **Tactical Empathy** | 3 | Mirror, label, ask calibrated How/What questions |
| **Smart Brevity** | 4 | Strong headline → why it matters → short details |
