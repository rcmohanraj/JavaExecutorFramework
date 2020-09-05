package com.rcmcode.concurrency.search;

import com.rcmcode.concurrency.LongTask;

public class SearchDAO {

    public void executeSearch() {
        LongTask.simulate(20_000);
    }
}
