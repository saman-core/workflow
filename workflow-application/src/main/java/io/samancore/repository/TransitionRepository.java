package io.samancore.repository;

import io.samancore.data.TransitionEntity;

import java.util.List;

public interface TransitionRepository {

    List<TransitionEntity> getAll();
}
