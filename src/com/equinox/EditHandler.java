package com.equinox;

import java.util.ArrayList;

public class EditHandler {
	
	public static Signal process(ParsedInput input, Memory memory) {
		ArrayList<KeyParamPair> paramPairList = input.getParamPairList();
		int userIndex = Integer.parseInt(paramPairList.get(0).getParam());
		memory.saveCurrentState();
		Todo editing = memory.get(userIndex);
		
		try {
			for (int i = 1; i < paramPairList.size(); i++) {
				String keyword = paramPairList.get(i).getKeyword();
				String param = paramPairList.get(i).getParam();

				switch (keyword) {
				case "title":
					editing.setTitle(param);
					break;
				case "start":
					editing.setStartTime(DateParser.parseDate(param));
					break;
				case "end":
					editing.setEndTime(DateParser.parseDate(param));
					break;
				case "done":
					editing.setDone(Boolean.parseBoolean(param));
					break;
				}
			}
			if(!editing.isValid()) {
				try {
					memory.restoreHistoryState();
				} catch (StateUndefinedException e) {
					e.printStackTrace();
				}
				return new Signal(Signal.SIGNAL_INVALID_PARAMS);
			}
			
			
		} catch (DateUndefinedException e) {
			return new Signal(Signal.SIGNAL_INVALID_PARAMS);
		}
		return new Signal(Signal.SIGNAL_SUCCESS);
	}

}
