package com.pedroluizforlan.rectiveflashcards.domain.mapper;

import com.pedroluizforlan.rectiveflashcards.domain.document.Card;
import com.pedroluizforlan.rectiveflashcards.domain.document.Question;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyCard;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface StudyDomainMapper {

   StudyCard toStudyCards(final Card cards);

   default Question generateRandomQuestion(final Set<StudyCard> cards ){
      var values = new ArrayList<>(cards);
      var random = new Random();
      var position = random.nextInt(values.size());
      return toQuestion(values.get(position));
   }

   @Mapping(target = "asked", source = "front")
   @Mapping(target = "answered", ignore = true)
   @Mapping(target = "answeredIn", ignore = true)
   @Mapping(target = "expected", source = "back")
   Question toQuestion(final StudyCard studyCard);

   default StudyDocument answer(final StudyDocument document, final String answer){
      var currentQuestion = document.getLastPendingQuestion();
      var questions = document.questionList();
      var curIndexQuestion = questions.indexOf(currentQuestion);

      currentQuestion = currentQuestion.toBuilder().answered(answer).build();
      questions.set(curIndexQuestion, currentQuestion);
      return document.toBuilder().questionList(questions).build();
   }
}
