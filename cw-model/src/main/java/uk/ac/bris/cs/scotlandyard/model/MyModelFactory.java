package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * cw-model
 * Stage 2: Complete this class
 */
public final class MyModelFactory implements Factory<Model> {

	@Nonnull @Override public Model build(GameSetup setup,
										  Player mrX,
										  ImmutableList<Player> detectives) {
		// TODO
		return new Model() {
			private Board.GameState gameState = new MyGameStateFactory().build(setup, mrX, detectives);
			private final List<Observer> observers = new ArrayList<>();

			@Nonnull
			@Override
			public Board getCurrentBoard() {
				return gameState;
			}

			@Override
			public void registerObserver(@Nonnull Observer observer) {
				if(observer.equals(null)){ // needed to pass test despite being flagged as @nonnull
					throw new NullPointerException();
				}
				if(observers.contains(observer)){
					throw new IllegalArgumentException("Observer already registered");
				}
				observers.add(observer);
			}

			@Override
			public void unregisterObserver(@Nonnull Observer observer) {
				if(observer.equals(null)){ // needed to pass test despite being flagged as @nonnull
					throw new NullPointerException();
				}
				if(!observers.contains(observer)){
					throw new IllegalArgumentException("Observer is not registered");
				}
				observers.remove(observer);
			}

			@Nonnull
			@Override
			public ImmutableSet<Observer> getObservers() {
				return ImmutableSet.copyOf(observers);
			}

			@Override
			public void chooseMove(@Nonnull Move move) {

				Observer.Event event;
				gameState = gameState.advance(move);

				if(!gameState.getWinner().isEmpty()){
					event = Observer.Event.GAME_OVER;
				}
				else {
					event = Observer.Event.MOVE_MADE;
				}

				for (Observer observer : observers) {
					observer.onModelChanged(gameState, event);
				}
			}
		};
	}
}
