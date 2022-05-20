UPDATE tbl_decode_input_abi SET abi = '{\"hash\":\"0000000000000000000000000000000000000001\",\"functions\":[{\"name\":\"init\",\"parameters\":[],\"returntype\":\"Bool\"},{\"name\":\"transfer\",\"parameters\":[{\"name\":\"transfers\",\"type\":\"Struct\",\"subType\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}]}],\"returntype\":\"Bool\"},{\"name\":\"transferV2\",\"parameters\":[{\"name\":\"transfers\",\"type\":\"Struct\",\"subType\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}]}],\"returntype\":\"Bool\"},{\"name\":\"approve\",\"parameters\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"approveV2\",\"parameters\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"transferFrom\",\"parameters\":[{\"name\":\"sender\",\"type\":\"Address\"},{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"transferFromV2\",\"parameters\":[{\"name\":\"sender\",\"type\":\"Address\"},{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"}]}' WHERE contract_hash = '0100000000000000000000000000000000000000';
UPDATE tbl_decode_input_abi SET abi = '{\"hash\":\"0000000000000000000000000000000000000002\",\"functions\":[{\"name\":\"init\",\"parameters\":[],\"returntype\":\"Bool\"},{\"name\":\"transfer\",\"parameters\":[{\"name\":\"transfers\",\"type\":\"Struct\",\"subType\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}]}],\"returntype\":\"Bool\"},{\"name\":\"transferV2\",\"parameters\":[{\"name\":\"transfers\",\"type\":\"Struct\",\"subType\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"BigInt\"}]}],\"returntype\":\"Bool\"},{\"name\":\"approve\",\"parameters\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"approveV2\",\"parameters\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"BigInt\"}],\"returntype\":\"Bool\"},{\"name\":\"transferFrom\",\"parameters\":[{\"name\":\"sender\",\"type\":\"Address\"},{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"transferFromV2\",\"parameters\":[{\"name\":\"sender\",\"type\":\"Address\"},{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"BigInt\"}],\"returntype\":\"Bool\"}]}' WHERE contract_hash = '0200000000000000000000000000000000000000';
UPDATE tbl_decode_input_abi SET abi = '{\"hash\":\"0000000000000000000000000000000000000003\",\"functions\":[{\"name\":\"regIDWithPublicKey\",\"parameters\":[{\"name\":\"ont id\",\"type\":\"String\"},{\"name\":\"pubKey\",\"type\":\"ByteArray\"}],\"returntype\":\"Bool\"},{\"name\":\"revokeID\",\"parameters\":[{\"name\":\"ont id\",\"type\":\"String\"},{\"name\":\"index\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"}]}' WHERE contract_hash = '0300000000000000000000000000000000000000';
UPDATE tbl_decode_input_abi SET abi = '{\"hash\":\"0000000000000000000000000000000000000007\",\"functions\":[{\"name\":\"registerCandidate\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"initPos\",\"type\":\"Uint64\"},{\"name\":\"ontId\",\"type\":\"String\"},{\"name\":\"keyNo\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"unRegisterCandidate\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"}],\"returntype\":\"Bool\"},{\"name\":\"withdrawOng\",\"parameters\":[{\"name\":\"account\",\"type\":\"Address\"}],\"returntype\":\"Bool\"},{\"name\":\"withdrawFee\",\"parameters\":[{\"name\":\"account\",\"type\":\"Address\"}],\"returntype\":\"Bool\"},{\"name\":\"approveCandidate\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"}],\"returntype\":\"Bool\"},{\"name\":\"rejectCandidate\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"}],\"returntype\":\"Bool\"},{\"name\":\"authorizeForPeer\",\"parameters\":[{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"peerPubkeyLength\",\"type\":\"Uint64\"},{\"name\":\"peerPubkey\",\"type\":\"String[]\",\"subType\":{\"name\":\"peerPubkey\",\"type\":\"String\"}},{\"name\":\"posListLength\",\"type\":\"Uint64\"},{\"name\":\"posList\",\"type\":\"Uint64[]\",\"subType\":{\"name\":\"pos\",\"type\":\"Uint64\"}}],\"returntype\":\"Bool\"},{\"name\":\"unAuthorizeForPeer\",\"parameters\":[{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"peerPubkeyLength\",\"type\":\"Uint64\"},{\"name\":\"peerPubkey\",\"type\":\"String[]\",\"subType\":{\"name\":\"peerPubkey\",\"type\":\"String\"}},{\"name\":\"posListLength\",\"type\":\"Uint64\"},{\"name\":\"posList\",\"type\":\"Uint64[]\",\"subType\":{\"name\":\"pos\",\"type\":\"Uint64\"}}],\"returntype\":\"Bool\"},{\"name\":\"withdraw\",\"parameters\":[{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"peerPubkeyLength\",\"type\":\"Uint64\"},{\"name\":\"peerPubkey\",\"type\":\"String[]\",\"subType\":{\"name\":\"peerPubkey\",\"type\":\"String\"}},{\"name\":\"withdrawListLength\",\"type\":\"Uint64\"},{\"name\":\"withdrawList\",\"type\":\"Uint64[]\",\"subType\":{\"name\":\"pos\",\"type\":\"Uint64\"}}],\"returntype\":\"Bool\"},{\"name\":\"commitDpos\",\"parameters\":[],\"returntype\":\"Bool\"},{\"name\":\"blackNode\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"}],\"returntype\":\"Bool\"},{\"name\":\"whiteNode\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"}],\"returntype\":\"Bool\"},{\"name\":\"quitNode\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"}],\"returntype\":\"Bool\"},{\"name\":\"changeMaxAuthorization\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"maxAuthorize\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"addInitPos\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"pos\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"reduceInitPos\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"pos\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"setPeerCost\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"peerCost\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"setFeePercentage\",\"parameters\":[{\"name\":\"peerPubkey\",\"type\":\"String\"},{\"name\":\"account\",\"type\":\"Address\"},{\"name\":\"peerCost\",\"type\":\"Uint64\"},{\"name\":\"stakeCost\",\"type\":\"Uint64\"}],\"returntype\":\"Bool\"},{\"name\":\"callSplit\",\"parameters\":[],\"returntype\":\"Bool\"}]}' WHERE contract_hash = '0700000000000000000000000000000000000000';
UPDATE tbl_decode_input_abi SET abi = '{\"functions\":[{\"name\":\"transfer\",\"parameters\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"amount\",\"type\":\"BigInt\"}],\"returntype\":\"Boolean\"},{\"name\":\"approve\",\"parameters\":[{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"BigInt\"}],\"returntype\":\"Bool\"},{\"name\":\"transferFrom\",\"parameters\":[{\"name\":\"sender\",\"type\":\"Address\"},{\"name\":\"from\",\"type\":\"Address\"},{\"name\":\"to\",\"type\":\"Address\"},{\"name\":\"value\",\"type\":\"BigInt\"}],\"returntype\":\"Bool\"}]}' WHERE contract_hash = 'neovm';