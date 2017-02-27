-- update some sortmembers
UPDATE listviews SET sortmember='EstimatedCloseDate' WHERE listtype='Opportunity';
UPDATE listviews SET sortmember='EstimatedCloseDate'  WHERE listtype='Proposal';
