package ryuunta.iot.ryuuntaesp.data.model

sealed class ScenarioItem() : RItem() {
    class QuickScenario(
        val label: String,
        val bgColor: Int? = null
    ) : ScenarioItem()
}