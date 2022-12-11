import { Table } from "react-bootstrap"


const CustomizableTableEntry = ({fields, buttons}) => {

    return (
        <tr>
            { fields.map( (field, key) => <td key={key}>{field}</td>) }
            <td>{buttons}</td>
        </tr>
    )

}

const CustomizableTable = ({headers, entries}) => {

    return (

        <Table striped bordered hover>
            <thead>
                <tr>
                    { headers.map( (header, key) => <th key={key}>{header}</th> ) }
                </tr>
            </thead>
            <tbody>
                {entries}
            </tbody>
        </Table>
    )
}


export { CustomizableTable, CustomizableTableEntry }