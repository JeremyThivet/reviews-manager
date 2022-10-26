package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.handler;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.FieldRepository;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.ScoreFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScoreFieldHandler implements FieldHandler{

    @Autowired
    private ScoreFieldRepository scoreFieldRepository;

    @Override
    public boolean canHandle(FieldType fieldType) {
        return fieldType == FieldType.SCORE;
    }

    @Override
    public FieldRepository getRepository() {
        return scoreFieldRepository;
    }
}
