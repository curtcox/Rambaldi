package tests.acceptance.ionic;

import com.asynchrony.ionicmobile.Avatar;
import com.asynchrony.ionicmobile.Poll;
import com.asynchrony.ionicmobile.User;
import net.rambaldi.http.HttpResponse;
import net.rambaldi.process.Timestamp;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static net.rambaldi.http.HttpResponse.Status.*;
import static org.junit.Assert.assertEquals;

/**
  Poll API's

  API's that allow real time polls to take place across the ionic mobile server.

  A poll is the containing resource of questions, choices, participants, and answers.

  A poll is by default unlocked, meaning it is open for participation.
  When a poll becomes locked, it is preferred to make the poll read only, but is not enforced by the server.

 See https://github.com/ionicmobile/ionic_api_server/blob/master/app/views/api_docs/general.html.haml
 */
public class Polls_Test {

    @Test
    public void I_should_be_able_to_create_a_poll_with_a_title() throws Exception {
        HttpResponse response = post("polls.json",json("poll",json("title","the title of the poll")));
        assertEquals(Created, response.status);
        Poll poll = parse(response.content,Poll.class);
        assertExpectedPoll(poll);
    }

    void assertExpectedPoll(Poll poll) throws URISyntaxException {
        assertEquals(new URI("uri_for_poll"),poll.uri);
        assertEquals("poll's simple id. Used for user input",poll.simple_id);
        assertEquals("the title of the poll",poll.title);
        assertEquals(3,poll.number_of_participants);
        assertEquals(true,poll.locked);
        assertEquals(new Timestamp(0),poll.created_at);
        User user = poll.owner;
        assertEquals("adam",user.username);
        assertEquals("Adam",user.first_name);
        assertEquals("Smith",user.last_name);
        assertEquals("email",user.email);
        assertEquals(new URI("for_user"),user.uri);
        Avatar avatar = user.avatar;
        assertEquals(new URI("for_avatar"),avatar.uri);
        assertEquals(new URI("for_avatar_thumbnail"),avatar.thumbnail_uri);
        assertEquals(new URI("for_avatar_small"),avatar.small_uri);
    }

    private Poll parse(String content, Class<Poll> pollClass) {
        return null;
    }

    String json(String... parts) {
        return "";
    }

    HttpResponse post(String url, String json) {
        return HttpResponse.builder().build();
    }

    HttpResponse get(String url) {
        return HttpResponse.builder().build();
    }

    @Test
    public void I_should_not_be_able_to_create_a_poll_without_a_title() throws Exception {
        HttpResponse response = post("polls.json",json("poll",json("title","")));
        assertEquals(BadRequest,response.status);
    }

    @Test
    public void I_should_be_able_to_get_a_poll() throws Exception {
        HttpResponse response = get("1.json");
        assertEquals(OK, response.status);
        Poll poll = parse(response.content,Poll.class);
        assertExpectedPoll(poll);
    }

//
//    %article
//            .anchorTarget#Polls-list
//    %h2 List all polls that were created by the authenticated user
//
//    %h3 Path
//    %p uses the #{link_to "poll's collection uri", "#Polls-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example polls_url(format: :json)
//    - extended = authenticated_resty_example polls_path(format: :json)
//    = render 'authenticated_examples', simple: simple, extended: extended, id: 'Polls-list'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "polls": [
//        {
//            "uri": "#{link_to "the poll's member uri", "#Polls-URI"}",
//            "id": "poll's id (deprecated)",
//                "simple_id": "poll's simple id. Used for user input",
//                "title": "the title of the poll",
//                "number_of_participants": "3",
//                "owner": {
//            "username": "adam",
//                    "first_name": "Adam",
//                    "last_name": "Smith",
//                    "email": "adam@example.com",
//                    "uri": "#{link_to "user member uri", "#Users-URI"}",
//                    "avatar": {
//                "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//            }
//        },
//            "locked": "one of true, or false",
//                "created_at": "The time the poll was created"
//        },
//        ...
//        ]
//    }
//
//    %hr
//
//    %article
//            .anchorTarget#Polls-update
//    %h2 Update a Poll
//
//    %p fields are only updated if they are sent in the request.  To update only one field, exclude all others from the request data.
//
//    %h3 Path
//    %p uses the #{link_to "poll's member uri", "#Polls-URI"}
//
//    %h3 Request Type
//    %p PUT
//
//    %h3 Parameters
//    %p
//    %pre
//    :preserve
//    {
//        "poll": {
//        "title": "the title of the poll",
//                "locked": "one of true, or false"
//    }
//    }
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_url(1, format: :json), { poll: { title: 'name of poll' }}, 'PUT'
//            - extended = authenticated_resty_example poll_path(1, format: :json), { poll: { title: 'name of poll' }}, 'PUT'
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Polls-update'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "poll": {
//            "uri": "#{link_to "the poll's member uri", "#Polls-URI"}",
//            "id": "poll's id (deprecated)",
//                    "simple_id": "poll's simple id. Used for user input",
//                    "title": "the title of the poll",
//                    "number_of_participants": "3",
//                    "owner": {
//                "username": "adam",
//                        "first_name": "Adam",
//                        "last_name": "Smith",
//                        "email": "adam@example.com",
//                        "uri": "#{link_to "user member uri", "#Users-URI"}",
//                        "avatar": {
//                    "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                    "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                    "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//                }
//            },
//            "locked": "one of true, or false",
//                    "created_at": "The time the poll was created"
//        }
//    }
//    %h3 Failed Response Reasons
//    %p
//    %ul
//    %li Title can't be blank
//
//
//            %section
//            .anchorTarget#Questions
//            .hero-unit
//    %h1 Questions
//
//    %p Questions belong to a poll and provide the ability for participants to respond to specific questions of a poll.
//
//    %p
//    Questions can be one of three types.
//    %code 0 - Yes / No
//    ,
//            %code 1 - Free Form
//            , and
//    %code 2 - Likert
//            (like / dislike).
//
//            %article
//            .anchorTarget#Questions-URI
//    %h3 URI's
//            %table
//    %thead
//    %tr
//    %th Resource Type
//    %th URI
//    %tbody
//    %tr
//    %td Collection
//    %td #{poll_questions_path(':poll_id')}
//    %tr
//    %td Member
//    %td #{poll_question_path(':poll_id', ':id')}
//
//    %article
//            .anchorTarget#Questions-create
//    %h2 Create a Question for a poll
//
//    %h3 Path
//    %p uses the #{link_to "question's collection uri", "#Questions-URI"}
//
//    %h3 Request Type
//    %p POST
//
//    %h3 Parameters
//    %p
//    %pre
//    :preserve
//    {
//        "question": {
//        "value": "The textual content of the question",
//                "question_type": "one of 0, 1, or 2",
//                "order": "Order in which the question should appear in the poll"
//    }
//    }
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_questions_url(1, format: :json), { question: { value: 'what is the question', question_type: 1 }}, 'POST'
//            - extended = authenticated_resty_example poll_questions_path(1, format: :json), { question: { value: 'what is the question', question_type: 1 }}, 'POST'
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Questions-create'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 201
//    body: {
//        "question": {
//            "uri": "#{link_to "the question's member uri", "#Questions-URI"}",
//            "id": "resource id (deprecated)"
//        }
//    }
//    %h3 Failed Response Reasons
//    %p
//    %ul
//    %li Value can't be blank
//            %li Value can't be more than 1000 characters
//            %li Question type must be one of 0, 1, or 2
//
//            %hr
//
//    %article
//            .anchorTarget#Questions-list
//    %h2 List Questions for a poll
//
//    %h3 Path
//    %p uses the #{link_to "question's collection uri", "#Questions-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_questions_url(1, format: :json)
//    - extended = authenticated_resty_example poll_questions_path(1, format: :json)
//    = render 'authenticated_examples', simple: simple, extended: extended, id: 'Questions-list'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "questions": [
//        {
//            "uri": "#{link_to "the question's member uri", "#Questions-URI"}",
//            "id": "resource id (deprecated)",
//                "value": "The value of the question",
//                "question_type": "one of 0,1, or 2",
//                "order": "Order in which the question should appear in the poll"
//        },
//        {
//            "uri": "#{link_to "the question's member uri", "#Questions-URI"}",
//            "id": "resource id (deprecated)",
//                "value": "The value of the question",
//                "question_type": "one of 0,1, or 2",
//                "order": "Order in which the question should appear in the poll"
//        }
//        ]
//    }
//    %hr
//
//    %article
//            .anchorTarget#Questions-show
//    %h2 Shows a Question for a poll
//
//    %h3 Path
//    %p uses the #{link_to "question's member uri", "#Questions-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_question_url(1, example_id('question'), format: :json)
//            - extended = authenticated_resty_example poll_question_path(1, example_id('question'), format: :json)
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Questions-show'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "question": {
//            "uri": "#{link_to "the question's member uri", "#Questions-URI"}",
//            "id": "resource id (deprecated)",
//                    "value": "The value of the question",
//                    "question_type": "one of 0,1, or 2",
//                    "order": "Order in which the question should appear in the poll"
//        }
//    }
//
//    %section
//            .anchorTarget#Participation
//            .hero-unit
//    %h1 Participation
//
//    %p This resource is the participation of the authenticated user and a specific poll.
//
//            %article
//            .anchorTarget#Participation-URI
//    %h3 URI's
//            %table
//    %thead
//    %tr
//    %th Resource Type
//    %th URI
//    %tbody
//    %tr
//    %td Collection
//    %td #{participations_path}
//    %tr
//    %td Member
//    %td #{poll_participation_path(':poll_id')}
//
//    %article
//            .anchorTarget#Participation-update_status
//    %h2 Update the status you have with a poll
//
//    %h3 Path
//    %p uses the #{link_to "participation uri", "#Participation-URI"}
//
//    %h3 Request Type
//    %p PUT
//
//    %h3 Parameters
//    %p
//    %pre
//    :preserve
//    {
//        "participation": {
//        "status": "one of 'active' or 'inactive'"
//    }
//    }
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_participation_url(1, format: :json), { participation: { status: 'active' }}, 'PUT'
//            - extended = authenticated_resty_example poll_participation_path(1, format: :json), { participation: { status: 'active' }}, 'PUT'
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Participation-update_status'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "participation": {
//            "status": "one of 'active' or 'inactive'",
//                    "participant_uri": "#{link_to 'Participant member URI', "#Participants-URI"}",
//                    "poll": {
//                "uri": "#{link_to "the poll's member uri", "#Polls-URI"}",
//                "id": "resource id (deprecated)",
//                        "simple_id": "poll's simple id, used for user input",
//                        "title": "the title of the poll",
//                        "number_of_participants": "3",
//                        "owner": {
//                    "username": "adam",
//                            "first_name": "Adam",
//                            "last_name": "Smith",
//                            "email": "adam@example.com",
//                            "uri": "#{link_to "user member uri", "#Users-URI"}",
//                            "avatar": {
//                        "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                        "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                        "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//                    }
//                },
//                "locked": "true",
//                        "created_at": "The time the poll was created"
//            }
//        }
//    }
//
//    %h3 Failed Response Reasons
//    %p
//    %ul
//    %li Status can only be active or inactive
//    %li You cannot join or leave a poll you started
//
//    %hr
//
//    %article
//            .anchorTarget#Participation-join
//    %h2 Join a poll
//
//    %p
//    Uses the #{link_to 'Update Participation Status API', '#Participation-status'} with
//    %code active
//    as the status.
//
//    %hr
//
//    %article
//            .anchorTarget#Participation-leave
//    %h2 Leave a poll
//
//    %p
//    Uses the #{link_to 'Update Participation Status API', '#Participation-status'} with
//    %code inactive
//    as the status.
//
//    %hr
//
//    %article
//            .anchorTarget#Participation-list
//    %h2 Lists all polls the authenticated user is participating in
//
//    %h3 Path
//    %p uses the #{link_to "participation's collection uri", "#Participation-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example participations_url(format: :json)
//    - extended = authenticated_resty_example participations_path(format: :json)
//    = render 'authenticated_examples', simple: simple, extended: extended, id: 'Participation-list'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        participations: [
//        {
//            "status": "one of 'active' or 'inactive'",
//                "participant_uri": "#{link_to 'Participant member URI', "#Participants-URI"}",
//                "poll": {
//            "uri": "#{link_to "the poll's member uri", "#Polls-URI"}",
//            "id": "resource id (deprecated)",
//                    "simple_id": "poll's simple id, used for user input",
//                    "title": "the title of the poll",
//                    "number_of_participants": "3",
//                    "owner": {
//                "username": "adam",
//                        "first_name": "Adam",
//                        "last_name": "Smith",
//                        "email": "adam@example.com",
//                        "uri": "#{link_to "user member uri", "#Users-URI"}",
//                        "avatar": {
//                    "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                    "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                    "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//                }
//            },
//            "locked": "true",
//                    "created_at": "The time the poll was created"
//        }
//        },
//        ...
//        ]
//    }
//
//
//    %section
//            .anchorTarget#Participants
//            .hero-unit
//    %h1 Participants
//
//    %p Users who are participating in a poll
//
//    %article
//            .anchorTarget#Participants-URI
//    %h3 URI's
//            %table
//    %thead
//    %tr
//    %th Resource Type
//    %th URI
//    %tbody
//    %tr
//    %td Collection
//    %td #{poll_participants_path(':poll_id')}
//    %tr
//    %td Member
//    %td #{poll_participant_path(':poll_id', ':id')}
//    %article
//            .anchorTarget#Participants-list
//    %h2 List Participants
//
//    %h3 Path
//    %p uses the #{link_to "participant's collection uri", "#Participants-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_participants_url(1, format: :json)
//    - extended = authenticated_resty_example poll_participants_path(1, format: :json)
//    = render 'authenticated_examples', simple: simple, extended: extended, id: 'Participants-list'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        participants: [
//        {
//            "id": "resource id (deprecated)",
//                "status": "The participant status, one of 'active' or 'inactive'",
//                "username": "joe",
//                "first_name": "Joe",
//                "last_name": "Hainline",
//                "email": "joe@joe.joe",
//                "uri": "#{link_to "participant's member uri", "#Participants-URI"}",
//            "user_uri": "#{link_to "user's member uri", "#Users-URI"}",
//            "avatar": {
//            "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//            "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//            "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//        }
//        },
//        ...
//        ]
//    }
//    %article
//            .anchorTarget#Participants-show
//    %h2 Show Participant
//
//    %h3 Path
//    %p uses the #{link_to "participant's member uri", "#Participants-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_participant_url(1, example_id('participant id'), format: :json)
//            - extended = authenticated_resty_example poll_participant_path(1, example_id('participant id'), format: :json)
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Participants-show'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        participant: {
//            "id": "resource id (deprecated)",
//                    "status": "The participant status, one of 'active' or 'inactive'",
//                    "username": "joe",
//                    "first_name": "Joe",
//                    "last_name": "Hainline",
//                    "email": "joe@joe.joe",
//                    "uri": "#{link_to "participant's member uri", "#Participants-URI"}",
//            "user_uri": "#{link_to "user's member uri", "#Users-URI"}",
//            "avatar": {
//                "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//            }
//        }
//    }
//
//
//    %section
//            .anchorTarget#Answers
//            .hero-unit
//    %h1 Answers
//
//    %p Answers to questions for a poll
//
//    %article
//            .anchorTarget#Answers-URI
//    %h3 URI's
//            %table
//    %thead
//    %tr
//    %th Resource Type
//    %th URI
//    %tbody
//    %tr
//    %td Collection
//    %td #{poll_question_answers_path(':poll_id', ':question_id')}
//    %tr
//    %td Member
//    %td #{poll_question_answer_path(':poll_id', ':question_id', ':id')}
//
//    %article
//            .anchorTarget#Answers-create
//    %h2 Create an Answer
//
//    %h3 Path
//    %p uses the #{link_to "answer's collection uri", "#Answers-URI"}
//
//    %h3 Request Type
//    %p POST
//
//    %h3 Parameters
//    %p
//    %pre
//    :preserve
//    {
//        "answer": {
//        "text": "the content of the answer"
//    }
//    }
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_question_answers_url(1, example_id('question'), format: :json), { answer: { text: 'answer of the question' } }, 'POST'
//            - extended = authenticated_resty_example poll_question_answers_path(1, example_id('question'), format: :json), { answer: { text: 'answer of the question' } }, 'POST'
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Answers-create'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 201
//    body: {
//        "answer": {
//            "id": "resource id (deprecated)",
//                    "uri": "#{link_to "the answer's member uri", "#Answers-URI"}"
//        }
//    }
//    %h3 Failure Reasons
//    %p
//    %ul
//    %li Text can't be blank.
//
//            %hr
//
//    %article
//            .anchorTarget#Answers-list
//    %h2 List a poll's answers
//
//            %h3 Path
//    %p uses the #{link_to "answer's collection uri", "#Answers-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_question_answers_url(1, example_id('question'), format: :json)
//            - extended = authenticated_resty_example poll_question_answers_path(1, example_id('question'), format: :json)
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Answers-list'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "answers": [
//        {
//            "id": "resource id (deprecated)",
//                "uri": "#{link_to "the answer's member uri", "#Answers-URI"}",
//            "text": "This is some text",
//                "created_at": "the time the answer was submitted",
//                "participant": {
//            "id": "resource id (deprecated)",
//                    "status": "The participant status, one of 'active' or 'inactive'",
//                    "username": "joe 2",
//                    "first_name": "Joe 2",
//                    "last_name": "Hainline2",
//                    "email": "joe@joe.joe",
//                    "uri": "#{link_to "participant's member uri", "#Participants-URI"}",
//            "user_uri": "#{link_to "user's member uri", "#Users-URI"}",
//            "avatar": {
//                "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//            }
//        }
//        },
//        ...
//        ]
//    }
//
//    %hr
//
//    %article
//            .anchorTarget#Answers-show
//    %h2 Show an answer
//
//    %h3 Path
//    %p uses the #{link_to "answer's member uri", "#Answers-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_question_answer_url(1, example_id('question'), example_id('answer'), format: :json)
//            - extended = authenticated_resty_example poll_question_answer_path(1, example_id('question'), example_id('answer'), format: :json)
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'Answers-show'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "answer": {
//            "id": "resource id (deprecated)",
//                    "uri": "#{link_to "the answer's member uri", "#Answers-URI"}",
//            "text": "This is some text",
//                    "created_at": "the time the answer was submitted",
//                    "participant": {
//                "id": "resource id (deprecated)",
//                        "status": "The participant status, one of 'active' or 'inactive'",
//                        "username": "joe 2",
//                        "first_name": "Joe 2",
//                        "last_name": "Hainline2",
//                        "email": "joe@joe.joe",
//                        "uri": "#{link_to "participant's member uri", "#Participant-URI"}",
//                "user_uri": "#{link_to "user's member uri", "#Users-URI"}",
//                "avatar": {
//                    "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                    "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                    "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//                }
//            }
//        }
//    }
//
//
//
//    %section
//            .anchorTarget#ParticipantAnswers
//            .hero-unit
//    %h1 Participant Answers
//
//    %p Answers created by a participant for a poll
//
//    %article
//            .anchorTarget#ParticipantAnswers-URI
//    %h3 URI's
//            %table
//    %thead
//    %tr
//    %th Resource Type
//    %th URI
//    %tbody
//    %tr
//    %td Collection
//    %td #{poll_participant_answers_path(':poll_id', ':participant_id')}
//
//    %article
//            .anchorTarget#ParticipantAnswers-list
//    %h2 List a poll's answers by a given participant
//
//            %h3 Path
//    %p uses the #{link_to "participant's member uri", "#Participants-URI"}
//
//    %h3 Request Type
//    %p GET
//
//    %h3 Curl Example
//    - simple = authenticated_curl_example poll_participant_answers_url(1, example_id('participant'), format: :json)
//            - extended = authenticated_resty_example poll_participant_answers_path(1, example_id('participant'), format: :json)
//            = render 'authenticated_examples', simple: simple, extended: extended, id: 'ParticipantAnswers-list'
//
//            %h3 Successful Response
//    %p
//    %pre
//    :preserve
//    status: 200
//    body: {
//        "answers": [
//        {
//            "id": "resource id (deprecated)",
//                "uri": "#{link_to "the answer's member uri", "#Answers-URI"}",
//            "text": "This is some text",
//                "created_at": "the time the answer was submitted",
//                "participant": {
//            "id": "resource id (deprecated)",
//                    "status": "The participant status, one of 'active' or 'inactive'",
//                    "username": "joe 2",
//                    "first_name": "Joe 2",
//                    "last_name": "Hainline2",
//                    "email": "joe@joe.joe",
//                    "uri": "#{link_to "participant's member uri", "#Participant-URI"}",
//            "user_uri": "#{link_to "user's member uri", "#Users-URI"}",
//            "avatar": {
//                "uri": "#{link_to "avatar's member uri", "#Avatars-URI"}",
//                "thumbnail_uri": "#{link_to "avatar's member thumbnail uri", "#Avatars-URI"}",
//                "small_uri": "#{link_to "avatar's member small uri", "#Avatars-URI"}"
//            }
//        }
//        },
//        ...
//        ]
//    }

}
