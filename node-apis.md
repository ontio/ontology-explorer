# Ontology Explorer Node APIs

- [Ontology Explorer Node APIs](#ontology-explorer-node-apis)
  - [Specification](#specification)
    - [URL](#url)
    - [Request](#request)
    - [Response](#response)
  - [APIs](#apis)
    - [Get all active node's informations in current network](#get-all-active-nodes-informations-in-current-network)
    - [Get all node's informations in current blockchain network](#get-all-nodes-informations-in-current-blockchain-network)
    - [Get the block count to next staking round](#get-the-block-count-to-next-staking-round)
    - [Get staking rewards per 10000 ONT](#get-staking-rewards-per-10000-ont)
    - [Get the count of candidate nodes in current blockchain network](#get-the-count-of-candidate-nodes-in-current-blockchain-network)
    - [Get the count of consensus nodes in current blockchain network](#get-the-count-of-consensus-nodes-in-current-blockchain-network)
    - [Get the count of nodes in current blockchain network](#get-the-count-of-nodes-in-current-blockchain-network)
    - [Get candidate nodes information in current blockchain network](#get-candidate-nodes-information-in-current-blockchain-network)
    - [Get the number of current staking round](#get-the-number-of-current-staking-round)
    - [Get the sum of total ONT stakes and the percentage total ONT stakes of total ONT market supply](#get-the-sum-of-total-ont-stakes-and-the-percentage-total-ont-stakes-of-total-ont-market-supply)
    - [Get latest reward per 10000 ONT stake unit by public key](#get-latest-reward-per-10000-ont-stake-unit-by-public-key)
    - [Get candidate and consensus nodes information with bonus in current blockchain network](#get-candidate-and-consensus-nodes-information-with-bonus-in-current-blockchain-network)
    - [Get candidate nodes information by name](#get-candidate-nodes-information-by-name)
    - [Get node register information by public key](#get-node-register-information-by-public-key)
    - [Get nodes register information in current blockchain network](#get-nodes-register-information-in-current-blockchain-network)
    - [Get candidate and consensus node information by public key](#get-candidate-and-consensus-node-information-by-public-key)
    - [Get the nodes rank change info](#get-the-nodes-rank-change-info)
    - [Get node history rank record](#get-node-history-rank-record)
    - [Get the count of synchronous nodes](#get-the-count-of-synchronous-nodes)

## Specification

### URL

All the API follow the REST style, define interfaces by resource.

### Request
 
Request parameters are in `SNAKE_CASE` style.

### Response

Response parameters are in `SNAKE_CASE`  style.

|Name|Schema|
|---|---|
|**code**  |integer (int32)|
|**msg**  |string|
|**result**  |object|

## APIs

### Get all active node's informations in current network

```
GET /v2/nodes/active-in-network
```

### Get all node's informations in current blockchain network

```
GET /v2/nodes/all-in-network
```

### Get the block count to next staking round

```
GET /v2/nodes/block-count-to-next-round
```

### Get block height and time of round history

```
GET /v2/nodes/round-history
```

### Get staking rewards per 10000 ONT

```
GET /v2/nodes/bonus-histories
```

### Get the count of candidate nodes in current blockchain network

```
GET /v2/nodes/candidate-node-count
```

### Get the count of consensus nodes in current blockchain network

```
GET /v2/nodes/consensus-node-count
```

### Get the count of nodes in current blockchain network

```
GET /v2/nodes/count
```

### Get candidate nodes information in current blockchain network

```
GET /v2/nodes/current-stakes
```

### Get the number of current staking round

```
GET /v2/nodes/current-staking-cycle
```

### Get the sum of total ONT stakes and the percentage total ONT stakes of total ONT market supply

```
GET /v2/nodes/current-total-stakes
```

### Get latest reward per 10000 ONT stake unit by public key

```
GET /v2/nodes/latest-bonus
```

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**address**  <br>*optional*|address|string|
|**Query**|**public_key**  <br>*optional*|public_key|string|


### Get candidate and consensus nodes information with bonus in current blockchain network

```
GET /v2/nodes/latest-bonuses-with-infos
```

### Get candidate nodes information by name

```
GET /v2/nodes/latest-bonuses-with-infos/search
```

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**name**  <br>*required*|name|string|

### Get node register information by public key

```
GET /v2/nodes/off-chain-info
```

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**public_key**  <br>*required*|public_key|string|

### Get nodes register information in current blockchain network

```
GET /v2/nodes/off-chain-infos
```

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**node_type**  <br>*optional*|node_type|integer (int32)|`-1`|

### Get candidate and consensus node information by public key

```
GET /v2/nodes/on-chain-info
```

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**public_key**  <br>*required*|public_key|string|

### Get the nodes rank change info

```
GET /v2/nodes/rank-change
```

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**is-desc**  <br>*optional*|is-desc|boolean|`"true"`|

### Get node history rank record

```
GET /v2/nodes/ranks/history
```

### Get the count of synchronous nodes

```
GET /v2/nodes/sync-node-count
```
