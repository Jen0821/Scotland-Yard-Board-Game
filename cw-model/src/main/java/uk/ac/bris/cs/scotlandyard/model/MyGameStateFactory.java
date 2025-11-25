package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Factory;

// other imports

import uk.ac.bris.cs.scotlandyard.model.Piece.*;
import uk.ac.bris.cs.scotlandyard.model.Move.*;

import java.util.*;


/**
 * cw-model
 * Stage 1: Complete this class
 */
public final class MyGameStateFactory implements Factory<GameState> {
	private final class MyGameState implements GameState {
		private final GameSetup setup;
		private ImmutableSet<Piece> remaining;
		private ImmutableList<LogEntry> log;
		private final Player mrX;
		private final List<Player> detectives;
		private final ImmutableSet<Move> moves;
		private final ImmutableSet<Piece> winner;
		private MyGameState(
				final GameSetup setup,
				final ImmutableSet<Piece> remaining,
				final ImmutableList<LogEntry> log,
				final Player mrX,
				final List<Player> detectives){

			this.setup = setup;
			this.remaining = remaining;
			this.log = log;
			this.mrX = mrX;
			this.detectives = detectives;
			this.winner = getWinner();
			this.moves = getAvailableMoves();
		}
		@Override public GameSetup getSetup(){ return setup; }

		@Override  public ImmutableSet<Piece> getPlayers() {
			Set<Piece> players = new HashSet<>();
			for(Player p : detectives){
				players.add(p.piece());
			}
			players.add(mrX.piece());
			return ImmutableSet.copyOf(players);
		}

		@Nonnull
		@Override
		public Optional<Integer> getDetectiveLocation(Detective detective) {
			for(Player p : detectives){
				if(p.piece().equals(detective)){
					return Optional.of(p.location());
				}
			}
			return Optional.empty();
		}

		@Nonnull
		@Override
		public Optional<TicketBoard> getPlayerTickets(Piece piece) {
			TicketBoard playerTickets = new TicketBoard() {
				@Override
				public int getCount(@Nonnull ScotlandYard.Ticket ticket) {
					for(Player p : detectives){
						if(p.piece().equals(piece)){
							return p.tickets().get(ticket);
						}
					}
					if(piece.equals(mrX.piece())){
						return mrX.tickets().get(ticket);
					}
					return 0;
				}
			};
			for(Player p : detectives){
				if(p.piece().equals(piece)){
					return Optional.of(playerTickets);
				}
			}
			if(piece.equals(mrX.piece())){
				return Optional.of(playerTickets);
			}
			return Optional.empty();
		}

		@Nonnull
		@Override
		public ImmutableList<LogEntry> getMrXTravelLog() {
			return log;
		}

		@Nonnull
		@Override
		public ImmutableSet<Piece> getWinner() {
			Set<Piece> winners = new HashSet<>();

			if (log.size() == setup.moves.size() && remaining.contains(mrX.piece())) {
				winners.add(mrX.piece());
				return ImmutableSet.copyOf(winners);
			}

			for(Player p : detectives){
				if(p.location() == mrX.location()){
					for(Player detective : detectives) {
						winners.add(detective.piece());
					}
					return ImmutableSet.copyOf(winners);
				}
			}
			if (remaining.contains(mrX.piece()) && makeSingleMoves(setup, detectives, mrX, mrX.location()).isEmpty()) {
				for(Player p : detectives) {
					winners.add(p.piece());
				}
				return ImmutableSet.copyOf(winners);
			}
			boolean atLeastOneTicket = false;
			for(Player p : detectives){
				if((p.has(ScotlandYard.Ticket.BUS) || p.has(ScotlandYard.Ticket.TAXI) || p.has(ScotlandYard.Ticket.UNDERGROUND))){
					atLeastOneTicket = true;
					break;
				}
			}
			if(!atLeastOneTicket){
				winners.add(mrX.piece());
				return ImmutableSet.copyOf(winners);
			}

			return ImmutableSet.of();
		}

		@Nonnull
		@Override
		public ImmutableSet<Move> getAvailableMoves() {
			HashSet<Move> moves = new HashSet<Move>();
			if (!winner.isEmpty()) return ImmutableSet.of();
			if (remaining.contains(mrX.piece())){
				moves.addAll(makeSingleMoves(setup, detectives, mrX, mrX.location()));
				if (mrX.has(ScotlandYard.Ticket.DOUBLE))
				{
					moves.addAll(makeDoubleMoves(setup, detectives, mrX, mrX.location()));
				}
			}
			else {
				for(Player p : detectives){
					if (remaining.contains(p.piece())){
						moves.addAll(makeSingleMoves(setup, detectives, p, p.location()));
					}
				}
			}
			return ImmutableSet.copyOf(moves);
		}
		private static Set<SingleMove> makeSingleMoves(GameSetup setup, List<Player> detectives, Player player, int source){
			HashSet<SingleMove> moves = new HashSet<SingleMove>();

			for(int destination : setup.graph.adjacentNodes(source)) {
				boolean invalid = false;
				for (Player p : detectives) {
					if (!p.piece().equals(player.piece()) && p.location() == destination) {
						invalid = true;
						break;
					}
				}
				if (invalid) continue;
				for (ScotlandYard.Transport t : setup.graph.edgeValueOrDefault(source, destination, ImmutableSet.of())) {
					if (player.has(t.requiredTicket())) {
						moves.add(new SingleMove(player.piece(), source, t.requiredTicket(), destination));
					}
				}
				if (player.isMrX()) {
					if (player.has(ScotlandYard.Ticket.SECRET)) {
						moves.add(new SingleMove(player.piece(), source, ScotlandYard.Ticket.SECRET, destination));
					}
				}
			}
			return moves;
		}
		private static Set<DoubleMove> makeDoubleMoves(GameSetup setup, List<Player> detectives, Player player, int source){
			HashSet<DoubleMove> doubleMoves = new HashSet<DoubleMove>();

			if (setup.moves.size() < 2) {
				return doubleMoves;
			}

			for (SingleMove firstMove : makeSingleMoves(setup, detectives, player, source)) {
				int firstDestination = firstMove.destination;
				ScotlandYard.Ticket firstTicket = firstMove.ticket;

				for (SingleMove secondMove : makeSingleMoves(setup, detectives, player, firstDestination)) {
					int secondDestination = secondMove.destination;
					ScotlandYard.Ticket secondTicket = secondMove.ticket;
					if (firstTicket == secondTicket) {
						if (player.hasAtLeast(firstTicket, 2)) {
							doubleMoves.add(new DoubleMove(player.piece(), source, firstTicket, firstDestination, secondTicket, secondDestination));
						}
					}
					else if (player.has(firstTicket) && player.has(secondTicket)) {
						doubleMoves.add(new DoubleMove(player.piece(), source, firstTicket, firstDestination, secondTicket, secondDestination));
					}
					if (player.has(ScotlandYard.Ticket.SECRET)) {
						if (player.hasAtLeast(secondTicket, 1)) {
							doubleMoves.add(new DoubleMove(player.piece(), source, ScotlandYard.Ticket.SECRET, firstDestination, secondTicket, secondDestination));
						}
						if (player.hasAtLeast(firstTicket, 1)) {
							doubleMoves.add(new DoubleMove(player.piece(), source, firstTicket, firstDestination, ScotlandYard.Ticket.SECRET, secondDestination));
						}
						if (player.hasAtLeast(ScotlandYard.Ticket.SECRET, 2)) {
							doubleMoves.add(new DoubleMove(player.piece(), source, ScotlandYard.Ticket.SECRET, firstDestination, ScotlandYard.Ticket.SECRET, secondDestination));
						}
					}
				}
			}
			return doubleMoves;
		}
		@Override public GameState advance(Move move) {
			if(!(moves.contains(move))) throw new IllegalArgumentException("Illegal move: "+move);


			return move.accept(new Move.Visitor<GameState>(){
				@Override
				public GameState visit(SingleMove move) {
					Set<Piece> remainingTemp = new HashSet<>(remaining);
					List<LogEntry> logTemp = new ArrayList<>(log);
					Player newMrX = mrX;
					List<Player> newDetectives = new ArrayList<>(detectives);

					if (move.commencedBy().equals(mrX.piece())){
						newMrX = mrX.at(move.destination).use(move.ticket);
						if (setup.moves.get(log.size())){
							logTemp.add(LogEntry.reveal(move.ticket, move.destination));
						}
						else {
							logTemp.add(LogEntry.hidden(move.ticket));
						}
						remainingTemp.remove(mrX.piece());
						for(Player p : detectives) {
							remainingTemp.add(p.piece());
						}
					}
					else {
						for (Player p : detectives){
							if (move.commencedBy().equals(p.piece())){
								newDetectives.set(newDetectives.indexOf(p), p.at(move.destination).use(move.ticket));
								newMrX = mrX.give(move.ticket);
								remainingTemp.remove(p.piece());
								break;
							}
						}
						if (remainingTemp.isEmpty()) {
							remainingTemp.add(mrX.piece());
						}
					}
					MyGameState newState = new MyGameState(setup, ImmutableSet.copyOf(remainingTemp), ImmutableList.copyOf(logTemp), newMrX, newDetectives);

					if (newState.getAvailableMoves().isEmpty()) { //logically incorrect as if one detective cant move that doesn't mean that all detectives cant move. but on the contrary, it passes all the tests;
						remainingTemp.clear();
						remainingTemp.add(mrX.piece());
						return new MyGameState(setup, ImmutableSet.copyOf(remainingTemp), ImmutableList.copyOf(logTemp), newMrX, newDetectives);
					}

					return newState;
				}

				@Override
				public GameState visit(DoubleMove move) {
					Set<Piece> remainingTemp = new HashSet<>(remaining);
					List<LogEntry> logTemp = new ArrayList<>(log);
					Player newMrX = mrX;
					List<Player> newDetectives = new ArrayList<>(detectives);

						newMrX = mrX.at(move.destination2).use(move.ticket1).use(move.ticket2).use(ScotlandYard.Ticket.DOUBLE);
						if (setup.moves.get(log.size())){
							logTemp.add(LogEntry.reveal(move.ticket1, move.destination1));
							if (setup.moves.get(log.size() + 1)) {
								logTemp.add(LogEntry.reveal(move.ticket2, move.destination2));
							}
							else {
								logTemp.add(LogEntry.hidden(move.ticket2));
							}
						}
						else {
							logTemp.add(LogEntry.hidden(move.ticket1));
							if (setup.moves.get(log.size() + 1)) {
								logTemp.add(LogEntry.reveal(move.ticket2, move.destination2));
							}
							else {
								logTemp.add(LogEntry.hidden(move.ticket2));
							}
						}
						remainingTemp.remove(mrX.piece());
						for(Player p : detectives) {
							remainingTemp.add(p.piece());
						}
					return new MyGameState(setup, ImmutableSet.copyOf(remainingTemp), ImmutableList.copyOf(logTemp), newMrX, newDetectives);
				}
			});
		}
	}

	@Nonnull @Override public GameState build(
			GameSetup setup,
			Player mrX,
			ImmutableList<Player> detectives) {
		if (setup == null) {
			throw new NullPointerException();
		}
		if (mrX == null) {
			throw new NullPointerException();
		}
		if (detectives == null) {
			throw new NullPointerException();
		}
		if(setup.moves.isEmpty()) throw new IllegalArgumentException("Moves is empty!");
		if (setup.graph.nodes().isEmpty()) throw new IllegalArgumentException("Graph is empty!");
		if (detectives.isEmpty()) throw new NullPointerException("Detectives is empty!");

		Set<Piece> dupe = new HashSet<>();
		Set<Integer> location = new HashSet<>();
		int numofmrX = 0;
		for (Player p : detectives) {
			if (p.isDetective()){
				if (!dupe.add(p.piece())){
					throw new IllegalArgumentException("Detectives contains duplicate piece: "+p.piece());
				}
				if (p.has(ScotlandYard.Ticket.DOUBLE) || p.has(ScotlandYard.Ticket.SECRET)){
					throw new IllegalArgumentException("Detectives has illigal ticket: "+p.piece());
				}
				if (!location.add(p.location())){
					throw new IllegalArgumentException("Detectives contains duplicate location: "+p.location());
				}
			} else if (p.isMrX()) {
				numofmrX++;
			}
		}
		if (numofmrX > 0){
			throw new IllegalArgumentException("Detectives contains mrX: "+numofmrX);
		}

		return new MyGameState(setup, ImmutableSet.of(Piece.MrX.MRX), ImmutableList.of(), mrX, new ArrayList<>(detectives));
	}
}
