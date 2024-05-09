type MinetifaceConf = {
	serverUrl: string;
	minimumFeedback: number;
	maximumFeedback: number;
	maximumScore: number;
	feedbackScoreLostPerTick: number;
	scoreLostPerTick: number;
	maximumSecondsKeepScore: number;
	fullMaxTime: number;
	fullMinTime: number;
	masochistEnabled: boolean;
	masochistMultiplier: number;
	masochistInstantPointsMultiplier: number;
	masochistDurationMultiplier: number;
	xpEnabled: boolean;
	xpMultiplier: number;
	xpInstantPointsMultiplier: number;
	xpDurationMultiplier: number;
	miningEnabled: boolean;
	minePointsMultiplier: number;
	mineInstantPointsMultiplier: number;
	mineDurationMultiplier: number;
	blocksScore: any;
	//  {
	//   block.minecraft.deepslate_iron_ore : {
	//     translationKey : block.minecraft.deepslate_iron_ore,
	//     name : Deepslate Iron Ore,
	//     score : 10.0
	//   },
	// },
	defaultBlockScore: number;
	attackEnabled: boolean;
	attackMultiplier: number;
	attackInstantPointsMultiplier: number;
	attackDurationMultiplier: number;
};
